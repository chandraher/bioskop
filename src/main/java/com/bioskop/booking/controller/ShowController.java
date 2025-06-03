package com.bioskop.booking.controller;

import com.bioskop.booking.dto.ShowListFilterDto;
import com.bioskop.booking.dto.ShowListResponseDto;
import com.bioskop.booking.dto.ShowAvailableSheatsResponseDto;
import com.bioskop.booking.dto.ShowAddRequestDto;
import com.bioskop.booking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowService showService;

    @GetMapping
    public List<ShowListResponseDto> listShows(
            @RequestParam(required = false) String date_show,
            @RequestParam(required = false) Integer cinema_hall_id,
            @RequestParam(required = false) Integer theater_id
    ) {
        ShowListFilterDto filter = new ShowListFilterDto();
        filter.setDate_show(date_show);
        filter.setCinema_hall_id(cinema_hall_id);
        filter.setTheater_id(theater_id);
        return showService.getShows(filter);
    }

    @GetMapping("/{showId}/available-sheats")
    public ShowAvailableSheatsResponseDto getAvailableSheatsByShowId(@PathVariable Integer showId) {
        return showService.getAvailableSheatsByShowId(showId);
    }

    @PostMapping
    public String addShow(@RequestBody ShowAddRequestDto dto) {
        return showService.addShow(dto);
    }
}
