package com.bioskop.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieResponseDto {
    private Integer id;
    private String title;
    private String rating;
    private String duration;
    private String genre;
    private String urlImage;
}
