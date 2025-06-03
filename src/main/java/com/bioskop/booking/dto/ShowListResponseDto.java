package com.bioskop.booking.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ShowListResponseDto {
    private Integer id_show;
    private Integer theater_id;
    private String theater_name;
    private Integer movie_id;
    private String movie_name;
    private String date_show;
    private String status;
    private BigDecimal price;
    private List<ShowTimeDto> list_time;

    @Data
    public static class ShowTimeDto {
        private String start_time;
        private String end_time;
    }
}
