package com.bioskop.booking.controller;

import com.bioskop.booking.dto.CinemaHallRequestDto;
import com.bioskop.booking.dto.CinemaHallResponseDto;
import com.bioskop.booking.service.interfaces.ICinemaHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cinema-halls")
public class CinemaHallController {
    @Autowired
    private ICinemaHallService cinemaHallService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated CinemaHallRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Validasi gagal");
            return ResponseEntity.badRequest().body(msg);
        }
        return ResponseEntity.ok(cinemaHallService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Validated CinemaHallRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Validasi gagal");
            return ResponseEntity.badRequest().body(msg);
        }
        return ResponseEntity.ok(cinemaHallService.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CinemaHallResponseDto> result = cinemaHallService.list(name, city, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        return ResponseEntity.ok(cinemaHallService.getDetail(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        cinemaHallService.delete(id);
        return ResponseEntity.ok("CinemaHall berhasil dihapus");
    }
}
