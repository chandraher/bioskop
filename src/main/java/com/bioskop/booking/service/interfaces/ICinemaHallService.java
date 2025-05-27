package com.bioskop.booking.service.interfaces;

import com.bioskop.booking.dto.CinemaHallRequestDto;
import com.bioskop.booking.dto.CinemaHallResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICinemaHallService {
    CinemaHallResponseDto create(CinemaHallRequestDto dto);
    CinemaHallResponseDto update(Integer id, CinemaHallRequestDto dto);
    void delete(Integer id);
    CinemaHallResponseDto getDetail(Integer id);
    Page<CinemaHallResponseDto> list(String name, String city, Pageable pageable);
}
