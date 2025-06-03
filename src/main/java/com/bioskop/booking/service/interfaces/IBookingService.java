package com.bioskop.booking.service.interfaces;

import com.bioskop.booking.dto.BookingAddRequestDto;
import com.bioskop.booking.dto.BookingAddResponseDto;

public interface IBookingService {
    BookingAddResponseDto addBooking(BookingAddRequestDto dto);
}
