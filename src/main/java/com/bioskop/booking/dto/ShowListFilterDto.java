package com.bioskop.booking.dto;

import lombok.Data;

@Data
public class ShowListFilterDto {
    private String date_show; // format yyyy-MM-dd
    private Integer cinema_hall_id;
    private Integer theater_id;
}
