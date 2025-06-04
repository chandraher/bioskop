package com.bioskop.booking.service;

import com.bioskop.booking.dto.BookingAddRequestDto;
import com.bioskop.booking.dto.BookingAddResponseDto;
import com.bioskop.booking.model.Booking;
import com.bioskop.booking.model.BookingSheat;
import com.bioskop.booking.model.Sheat;
import com.bioskop.booking.repository.BookingSheatRepository;
import com.bioskop.booking.repository.BookingRepository;
import com.bioskop.booking.repository.SheatRepository;
import com.bioskop.booking.repository.ShowRepository;
import com.bioskop.booking.repository.UsersRepository;
import com.bioskop.booking.model.Show;
import com.bioskop.booking.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService implements com.bioskop.booking.service.interfaces.IBookingService {
    private final BookingRepository bookingRepository;
    private final BookingSheatRepository bookingSheatRepository;
    private final SheatRepository sheatRepository;
    private final ShowRepository showRepository;
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public BookingAddResponseDto addBooking(BookingAddRequestDto dto) {
        BookingAddResponseDto response = new BookingAddResponseDto();
        // 1. Validasi mandatory
        if (dto.getShows_id() == null || dto.getUsers_id() == null || dto.getBooking_sheats() == null || dto.getBooking_sheats().isEmpty()) {
            response.setSuccess(false);
            response.setMessage("All fields are mandatory");
            return response;
        }
        Optional<Show> showOpt = showRepository.findById(dto.getShows_id());
        if (showOpt.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Show not found");
            return response;
        }
        Optional<Users> userOpt = usersRepository.findById(dto.getUsers_id());
        if (userOpt.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("User not found");
            return response;
        }
        Show show = showOpt.get();
        Users user = userOpt.get();

        // 2. Validasi kursi sudah dibooking
        List<Integer> sheatsIdReq = new ArrayList<>();
        for (var s : dto.getBooking_sheats()) sheatsIdReq.add(s.getSheats_id());
        List<Sheat> sheats = sheatRepository.findAllById(sheatsIdReq);
        if (sheats.size() != sheatsIdReq.size()) {
            response.setSuccess(false);
            response.setMessage("Some sheats not found");
            return response;
        }
        List<Sheat> alreadyBooked = bookingSheatRepository.findBookedSheatsByShowId(dto.getShows_id());
        for (Sheat s : sheats) {
            if (alreadyBooked.stream().anyMatch(bs -> bs.getId().equals(s.getId()))) {
                response.setSuccess(false);
                response.setMessage("Sheat with id " + s.getId() + " is already booked");
                return response;
            }
        }

        // 3. Validasi aturan TIX ID (tidak boleh menyisakan satu kursi kosong di sebelah)
        // Ambil semua kursi di row & column yang sama pada theater
        Map<Integer, List<Sheat>> rowMap = new HashMap<>();
        Map<Integer, List<Sheat>> colMap = new HashMap<>();
        for (Sheat s : sheatRepository.findByTheater(show.getTheater())) {
            rowMap.computeIfAbsent(s.getRow(), k -> new ArrayList<>()).add(s);
            colMap.computeIfAbsent(s.getColumn(), k -> new ArrayList<>()).add(s);
        }
        Set<Integer> selectedSheats = new HashSet<>(sheatsIdReq);
        Set<Integer> bookedSheats = new HashSet<>();
        for (Sheat s : alreadyBooked) bookedSheats.add(s.getId());

        for (Sheat s : sheats) {
            // Cek kiri-kanan
            for (int d = -1; d <= 1; d += 2) {
                int neighborCol = s.getColumn() + d;
                Sheat neighbor = rowMap.get(s.getRow()).stream().filter(x -> x.getColumn() == neighborCol).findFirst().orElse(null);
                if (neighbor != null && !selectedSheats.contains(neighbor.getId()) && !bookedSheats.contains(neighbor.getId())) {
                    // Cek jika neighbor kiri/kanan kosong dan diapit booking
                    int farCol = s.getColumn() + 2 * d;
                    Sheat far = rowMap.get(s.getRow()).stream().filter(x -> x.getColumn() == farCol).findFirst().orElse(null);
                    if (far != null && (selectedSheats.contains(far.getId()) || bookedSheats.contains(far.getId()))) {
                        response.setSuccess(false);
                        response.setMessage("Tidak boleh booking dengan menyisakan satu kursi kosong di sebelah (aturan TIX ID)");
                        return response;
                    }
                }
            }
            // Cek atas-bawah (opsional, biasanya aturan hanya kiri-kanan, jika ingin bisa aktifkan)
            /*
            for (int d = -1; d <= 1; d += 2) {
                int neighborRow = s.getRow() + d;
                Sheat neighbor = colMap.get(s.getColumn()).stream().filter(x -> x.getRow() == neighborRow).findFirst().orElse(null);
                if (neighbor != null && !selectedSheats.contains(neighbor.getId()) && !bookedSheats.contains(neighbor.getId())) {
                    int farRow = s.getRow() + 2 * d;
                    Sheat far = colMap.get(s.getColumn()).stream().filter(x -> x.getRow() == farRow).findFirst().orElse(null);
                    if (far != null && (selectedSheats.contains(far.getId()) || bookedSheats.contains(far.getId()))) {
                        response.setSuccess(false);
                        response.setMessage("Tidak boleh booking dengan menyisakan satu kursi kosong di atas/bawah (aturan TIX ID)");
                        return response;
                    }
                }
            }
            */
        }

        // 4. Simpan booking
        Booking booking = new Booking();
        booking.setShows(show);
        booking.setUsers(user);
        booking.setStatus("unpaid");
        booking.setBookingDate(new Date());
        booking = bookingRepository.save(booking);
        for (Sheat s : sheats) {
            BookingSheat bs = new BookingSheat();
            bs.setBooking(booking);
            bs.setSheats(s);
            bookingSheatRepository.save(bs);
        }
        response.setSuccess(true);
        response.setMessage("Booking created successfully");
        response.setBooking_id(booking.getId());
        return response;
    }
}
