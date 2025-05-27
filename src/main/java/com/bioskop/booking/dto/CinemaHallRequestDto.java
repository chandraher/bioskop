package com.bioskop.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CinemaHallRequestDto {
    @NotBlank(message = "Name wajib diisi")
    private String name;
    @NotBlank(message = "City wajib diisi")
    private String city;
    @NotBlank(message = "Address wajib diisi")
    private String address;
}
