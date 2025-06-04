package com.bioskop.booking.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookingAddRequestDto {
    private Integer shows_id;
    private Integer users_id;
    private List<BookingSheatDto> booking_sheats;

    @Data
    public static class BookingSheatDto {
        private Integer sheats_id;
    }
}
