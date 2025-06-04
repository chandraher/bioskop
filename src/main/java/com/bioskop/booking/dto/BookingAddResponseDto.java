package com.bioskop.booking.dto;

import lombok.Data;

@Data
public class BookingAddResponseDto {
    private boolean success;
    private String message;
    private Integer booking_id;
}
