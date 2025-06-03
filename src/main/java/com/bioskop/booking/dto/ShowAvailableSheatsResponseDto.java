package com.bioskop.booking.dto;

import lombok.Data;
import java.util.List;

@Data
public class ShowAvailableSheatsResponseDto {
    private Integer theater_id;
    private String theater_name;
    private Integer movie_id;
    private String movie_name;
    private Integer max_row;
    private Integer max_column;
    private List<SheatAvailableDto> list_sheats_available;

    @Data
    public static class SheatAvailableDto {
        private Integer row;
        private Integer column;
        private Integer id_sheats;
        private String code;
    }
}
