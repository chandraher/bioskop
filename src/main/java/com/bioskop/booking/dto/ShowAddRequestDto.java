package com.bioskop.booking.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ShowAddRequestDto {
    private Integer theater_id;
    private Integer movie_id;
    private String date_show; // format yyyy-MM-dd
    private String status;
    private BigDecimal price;
    private List<ShowTimeDto> list_time;

    @Data
    public static class ShowTimeDto {
        private String start_time; // format HH:mm:ss
        private String end_time;   // format HH:mm:ss
    }
}
