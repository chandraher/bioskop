package com.bioskop.booking.service;

import com.bioskop.booking.dto.ShowAvailableSheatsResponseDto;
import com.bioskop.booking.dto.ShowListFilterDto;
import com.bioskop.booking.dto.ShowListResponseDto;
import com.bioskop.booking.model.Show;
import com.bioskop.booking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService implements com.bioskop.booking.service.interfaces.IShowService {
    private final ShowRepository showRepository;
    private final com.bioskop.booking.repository.TheaterRepository theaterRepository;
    private final com.bioskop.booking.repository.MovieRepository movieRepository;
    private final com.bioskop.booking.repository.SheatRepository sheatRepository;
    private final com.bioskop.booking.repository.BookingSheatRepository bookingSheatRepository;

    @Override
    public List<ShowListResponseDto> getShows(ShowListFilterDto filter) {
        Date dateShow = null;
        if (filter.getDate_show() != null && !filter.getDate_show().isEmpty()) {
            try {
                dateShow = new SimpleDateFormat("yyyy-MM-dd").parse(filter.getDate_show());
            } catch (ParseException ignored) {}
        }
        Integer cinemaHallId = filter.getCinema_hall_id();
        Integer theaterId = filter.getTheater_id();
        List<Show> shows = showRepository.findShowsByFilter(dateShow, null, cinemaHallId, theaterId);

        // Group by show id, then collect list_time
        Map<Integer, List<Show>> grouped = shows.stream().collect(Collectors.groupingBy(Show::getId));
        List<ShowListResponseDto> result = new ArrayList<>();
        for (List<Show> group : grouped.values()) {
            Show show = group.get(0);
            ShowListResponseDto dto = new ShowListResponseDto();
            dto.setId_show(show.getId());
            dto.setTheater_id(show.getTheater() != null ? show.getTheater().getId() : null);
            dto.setTheater_name(show.getTheater() != null ? show.getTheater().getName() : null);
            dto.setMovie_id(show.getMovie() != null ? show.getMovie().getId() : null);
            dto.setMovie_name(show.getMovie() != null ? show.getMovie().getTitle() : null);
            dto.setDate_show(show.getDateShow() != null ? new SimpleDateFormat("yyyy-MM-dd").format(show.getDateShow()) : null);
            dto.setStatus(show.getStatus());
            dto.setPrice(show.getPrice());
            List<ShowListResponseDto.ShowTimeDto> times = group.stream().map(s -> {
                ShowListResponseDto.ShowTimeDto t = new ShowListResponseDto.ShowTimeDto();
                t.setStart_time(s.getStartTime() != null ? new SimpleDateFormat("HH:mm:ss").format(s.getStartTime()) : null);
                t.setEnd_time(s.getEndTime() != null ? new SimpleDateFormat("HH:mm:ss").format(s.getEndTime()) : null);
                return t;
            }).collect(Collectors.toList());
            dto.setList_time(times);
            result.add(dto);
        }
        return result;
    }

    @Override
    public String addShow(com.bioskop.booking.dto.ShowAddRequestDto dto) {
        // 1. Validasi mandatory
        if (dto.getTheater_id() == null || dto.getMovie_id() == null || dto.getDate_show() == null || dto.getDate_show().isEmpty() ||
            dto.getStatus() == null || dto.getStatus().isEmpty() || dto.getPrice() == null || dto.getList_time() == null || dto.getList_time().isEmpty()) {
            return "All fields are mandatory";
        }

        // 2. Cek foreign key
        var theaterOpt = theaterRepository.findById(dto.getTheater_id());
        if (theaterOpt.isEmpty()) return "Theater is empty";
        var movieOpt = movieRepository.findById(dto.getMovie_id());
        if (movieOpt.isEmpty()) return "Movie is empty";

        // 3. Cek duplikasi kombinasi theater_id, movie_id, date_show
        java.util.Date dateShow;
        try {
            dateShow = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dto.getDate_show());
        } catch (Exception e) {
            return "Invalid date_show format";
        }
        var existingShows = showRepository.findShowsByFilter(dateShow, null, null, dto.getTheater_id());
        // boolean duplicate = existingShows.stream().anyMatch(s ->
        //     s.getMovie().getId().equals(dto.getMovie_id()) &&
        //     s.getTheater().getId().equals(dto.getTheater_id()) &&
        //     s.getDateShow().equals(dateShow)
        // );
        // if (duplicate) return "Show with same theater, movie, and date already exists";

        // 4. Validasi overlap waktu
        for (var timeDto : dto.getList_time()) {
            java.util.Date startTime, endTime;
            try {
                startTime = new java.text.SimpleDateFormat("HH:mm:ss").parse(timeDto.getStart_time());
                endTime = new java.text.SimpleDateFormat("HH:mm:ss").parse(timeDto.getEnd_time());
            } catch (Exception e) {
                return "Invalid start_time or end_time format";
            }
            for (var show : existingShows) {
                java.util.Date existStart = show.getStartTime();
                java.util.Date existEnd = show.getEndTime();
                if (existStart != null && existEnd != null &&
                    ((startTime.equals(existStart) || (startTime.after(existStart) && startTime.before(existEnd))) ||
                    (endTime.after(existStart) && endTime.before(existEnd)) ||
                    (startTime.before(existStart) && endTime.after(existEnd)))) {
                    return "Show time overlaps with existing show";
                }
            }
        }

        // 5. Insert show(s)
        for (var timeDto : dto.getList_time()) {
            try {
                var show = new com.bioskop.booking.model.Show();
                show.setTheater(theaterOpt.get());
                show.setMovie(movieOpt.get());
                show.setDateShow(dateShow);
                show.setStatus(dto.getStatus());
                show.setPrice(dto.getPrice());
                show.setStartTime(new java.text.SimpleDateFormat("HH:mm:ss").parse(timeDto.getStart_time()));
                show.setEndTime(new java.text.SimpleDateFormat("HH:mm:ss").parse(timeDto.getEnd_time()));
                showRepository.save(show);
            } catch (Exception e) {
                return "Failed to save show: " + e.getMessage();
            }
        }
        return "Show(s) added successfully";
    }

    @Override
    public ShowAvailableSheatsResponseDto getAvailableSheatsByShowId(Integer showId) {
        var showOpt = showRepository.findById(showId);
        if (showOpt.isEmpty()) return null;
        var show = showOpt.get();
        var theater = show.getTheater();
        var movie = show.getMovie();
        // Ambil semua kursi di theater
        var allSheats = sheatRepository.findByTheater(theater);
        // Ambil kursi yang sudah dibooking untuk show ini
        var bookedSheats = bookingSheatRepository.findBookedSheatsByShowId(showId);
        // Filter kursi yang belum dibooking
        var availableSheats = allSheats.stream()
            .filter(s -> bookedSheats.stream().noneMatch(bs -> bs.getId().equals(s.getId())))
            .toList();
        // Build response
        var resp = new com.bioskop.booking.dto.ShowAvailableSheatsResponseDto();
        resp.setTheater_id(theater.getId());
        resp.setTheater_name(theater.getName());
        resp.setMovie_id(movie.getId());
        resp.setMovie_name(movie.getTitle());
        resp.setMax_row(theater.getMaxRow());
        resp.setMax_column(theater.getMaxColumn());
        var list = new java.util.ArrayList<com.bioskop.booking.dto.ShowAvailableSheatsResponseDto.SheatAvailableDto>();
        for (var s : availableSheats) {
            var dto = new com.bioskop.booking.dto.ShowAvailableSheatsResponseDto.SheatAvailableDto();
            dto.setRow(s.getRow());
            dto.setColumn(s.getColumn());
            dto.setId_sheats(s.getId());
            dto.setCode(s.getCode());
            list.add(dto);
        }
        resp.setList_sheats_available(list);
        return resp;
    }
}
