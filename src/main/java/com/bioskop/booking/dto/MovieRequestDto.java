package com.bioskop.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class MovieRequestDto {
    private String title;
    private String rating;
    private Integer duration;
    private String genre;
    private MultipartFile urlImage;
}
