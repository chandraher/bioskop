package com.bioskop.booking.controller;

import com.bioskop.booking.dto.BookingAddRequestDto;
import com.bioskop.booking.dto.BookingAddResponseDto;
import com.bioskop.booking.service.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final IBookingService bookingService;

    @PostMapping
    public BookingAddResponseDto addBooking(@RequestBody BookingAddRequestDto dto) {
        return bookingService.addBooking(dto);
    }
}
