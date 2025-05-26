package com.bioskop.booking.controller;

import com.bioskop.booking.model.Movie;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/movies")
public class MovieController {
    // Untuk sementara, simulasikan list movie di memori
    private static final List<Movie> MOVIES = new ArrayList<>();

    static {
        // Dummy data
        MOVIES.add(new Movie(1L, "Inception", "Action", 8.8));
        MOVIES.add(new Movie(2L, "Interstellar", "Sci-Fi", 8.6));
        MOVIES.add(new Movie(3L, "The Godfather", "Crime", 9.2));
        MOVIES.add(new Movie(4L, "Coco", "Animation", 8.4));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) String genre
    ) {
        List<Movie> filtered = MOVIES.stream()
                .filter(m -> title == null || m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(m -> rating == null || m.getRating() >= rating)
                .filter(m -> genre == null || m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }
}
