package com.bioskop.booking.controller;

import com.bioskop.booking.dto.MovieRequestDto;
import com.bioskop.booking.dto.MovieResponseDto;
import com.bioskop.booking.dto.PagedResponseDto;
import com.bioskop.booking.model.Movie;
import com.bioskop.booking.service.interfaces.IMovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {
    static String uploadDir = "uploaded-movies";

    @Autowired
    private IMovieService movieService;

    @GetMapping
    public PagedResponseDto<MovieResponseDto> listMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Movie> moviePage = movieService.searchMovies(title, rating, genre, PageRequest.of(page, size));
        List<MovieResponseDto> content = moviePage.getContent().stream()
                .map(m -> new MovieResponseDto(
                        m.getId(),
                        m.getTitle(),
                        m.getRating(),
                        m.getDuration(),
                        m.getGenre(),
                        m.getUrlImage()
                ))
                .collect(Collectors.toList());
        return new PagedResponseDto<>(
                content,
                page,
                size,
                moviePage.getTotalElements(),
                moviePage.getTotalPages()
        );
    }


@PostMapping(value = "/add", consumes = "multipart/form-data")
public ResponseEntity<?> addMovie(
        @ModelAttribute MovieRequestDto dto) throws Exception {
    Movie movie = movieService.addMovie(dto, uploadDir);
    MovieResponseDto response = new MovieResponseDto(
            movie.getId(),
            movie.getTitle(),
            movie.getRating(),
            String.valueOf(movie.getDuration()),
            movie.getGenre(),
            movie.getUrlImage()
    );
    return ResponseEntity.ok(response);
}
//@ModelAttribute anotasi digunakan sebagai request body berbentuk objek/multipart
//@validated untuk validasi di DTO
//BindingResult untuk menangkap error validasi
@PutMapping(value = "/{id}", consumes = "multipart/form-data")
public ResponseEntity<?> updateMovie(
        @PathVariable Integer id,
        @ModelAttribute MovieRequestDto dto) throws Exception {
    Movie movie = movieService.updateMovie(id, dto, uploadDir);
    MovieResponseDto response = new MovieResponseDto(
            movie.getId(),
            movie.getTitle(),
            movie.getRating(),
            String.valueOf(movie.getDuration()),
            movie.getGenre(),
            movie.getUrlImage()
    );
    return ResponseEntity.ok(response);
}

@DeleteMapping("/{id}")
public ResponseEntity<?> deleteMovie(@PathVariable Integer id) throws Exception {
    movieService.deleteMovie(id);
    return ResponseEntity.ok("Movie berhasil dihapus");
}

}
