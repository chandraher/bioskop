package com.bioskop.booking.service;

import com.bioskop.booking.dto.CinemaHallRequestDto;
import com.bioskop.booking.dto.CinemaHallResponseDto;
import com.bioskop.booking.model.CinemaHall;
import com.bioskop.booking.repository.CinemaHallRepository;
import com.bioskop.booking.service.interfaces.ICinemaHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CinemaHallService implements ICinemaHallService {
    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Override
    public CinemaHallResponseDto create(CinemaHallRequestDto dto) {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setName(dto.getName());
        cinemaHall.setCity(dto.getCity());
        cinemaHall.setAddress(dto.getAddress());
        CinemaHall saved = cinemaHallRepository.save(cinemaHall);
        return new CinemaHallResponseDto(saved.getId(), saved.getName(), saved.getCity(), saved.getAddress());
    }

    @Override
    public CinemaHallResponseDto update(Integer id, CinemaHallRequestDto dto) {
        CinemaHall cinemaHall = cinemaHallRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CinemaHall tidak ditemukan"));
        cinemaHall.setName(dto.getName());
        cinemaHall.setCity(dto.getCity());
        cinemaHall.setAddress(dto.getAddress());
        CinemaHall saved = cinemaHallRepository.save(cinemaHall);
        return new CinemaHallResponseDto(saved.getId(), saved.getName(), saved.getCity(), saved.getAddress());
    }

    @Override
    public void delete(Integer id) {
        CinemaHall cinemaHall = cinemaHallRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CinemaHall tidak ditemukan"));
        cinemaHallRepository.delete(cinemaHall);
    }

    @Override
    public CinemaHallResponseDto getDetail(Integer id) {
        CinemaHall cinemaHall = cinemaHallRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CinemaHall tidak ditemukan"));
        return new CinemaHallResponseDto(cinemaHall.getId(), cinemaHall.getName(), cinemaHall.getCity(), cinemaHall.getAddress());
    }

    @Override
    public Page<CinemaHallResponseDto> list(String name, String city, Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<com.bioskop.booking.model.CinemaHall> spec = org.springframework.data.jpa.domain.Specification.where(null);
        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (city != null && !city.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
        }
        return cinemaHallRepository.findAll(spec, pageable)
                .map(ch -> new CinemaHallResponseDto(ch.getId(), ch.getName(), ch.getCity(), ch.getAddress()));
    }
}
