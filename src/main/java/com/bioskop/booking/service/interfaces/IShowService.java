package com.bioskop.booking.service.interfaces;

import com.bioskop.booking.dto.ShowListFilterDto;
import com.bioskop.booking.dto.ShowListResponseDto;
import java.util.List;

public interface IShowService {
    List<ShowListResponseDto> getShows(ShowListFilterDto filter);
}
