package com.bioskop.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CinemaHallResponseDto {
    private Integer id;
    private String name;
    private String city;
    private String address;
}
