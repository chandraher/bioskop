package com.bioskop.booking.service.interfaces;

import com.bioskop.booking.dto.ShowAddRequestDto;
import com.bioskop.booking.dto.ShowAvailableSheatsResponseDto;
import com.bioskop.booking.dto.ShowListFilterDto;
import com.bioskop.booking.dto.ShowListResponseDto;
import java.util.List;

public interface IShowService {
    List<ShowListResponseDto> getShows(ShowListFilterDto filter);
    String addShow(ShowAddRequestDto dto);
    ShowAvailableSheatsResponseDto getAvailableSheatsByShowId(Integer showId);
}
