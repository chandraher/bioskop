package com.bioskop.booking.service.interfaces;

import com.bioskop.booking.model.Movie;
import com.bioskop.booking.dto.MovieRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMovieService {
    Page<Movie> searchMovies(String title, String rating, String genre, Pageable pageable);
    Movie addMovie(MovieRequestDto dto, String uploadDir) throws Exception;
    Movie updateMovie(Integer id, MovieRequestDto dto, String uploadDir) throws Exception;
    void deleteMovie(Integer id) throws Exception;
}
