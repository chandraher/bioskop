package com.bioskop.booking.service;

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
}
