package com.bioskop.booking.service;

import com.bioskop.booking.model.Movie;
import com.bioskop.booking.repository.MovieRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bioskop.booking.dto.MovieRequestDto;

@Service
public class MovieService implements com.bioskop.booking.service.interfaces.IMovieService {
    @Autowired
    private MovieRepository movieRepository;

    public Page<Movie> searchMovies(String title, String rating, String genre, Pageable pageable) {
        Specification<Movie> spec = Specification.where(null);
        if (title != null && !title.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (rating != null && !rating.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("rating"), rating));
        }
        if (genre != null && !genre.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("genre")), genre.toLowerCase()));
        }
        return movieRepository.findAll(spec, pageable);
    }
    public Movie addMovie(MovieRequestDto dto, String uploadDir) throws Exception {
        // Title uniqueness validation
        if (movieRepository.existsByTitleIgnoreCase(dto.getTitle())) {
            throw new IllegalArgumentException("Movie dengan judul tersebut sudah ada");
        }
        // Validasi manual (jaga-jaga jika validation belum jalan di controller)
        if (dto.getTitle() == null || dto.getTitle().isBlank()) throw new IllegalArgumentException("Title wajib diisi");
        if (dto.getRating() == null || dto.getRating().isBlank()) throw new IllegalArgumentException("Rating wajib diisi");
        if (dto.getDuration() == null) throw new IllegalArgumentException("Duration wajib diisi");
        if (dto.getGenre() == null || dto.getGenre().isBlank()) throw new IllegalArgumentException("Genre wajib diisi");
        if (dto.getUrlImage() == null || dto.getUrlImage().isEmpty()) throw new IllegalArgumentException("urlImage wajib diisi");

        org.springframework.web.multipart.MultipartFile file = dto.getUrlImage();
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = java.util.UUID.randomUUID() + ext;
        java.nio.file.Path dirPath = java.nio.file.Paths.get(uploadDir).toAbsolutePath();
        if (!java.nio.file.Files.exists(dirPath)) {
            java.nio.file.Files.createDirectories(dirPath);
        }
        java.nio.file.Path filePath = dirPath.resolve(fileName);
        file.transferTo(filePath.toFile());

        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setRating(dto.getRating());
        movie.setDuration(dto.getDuration().toString());
        movie.setGenre(dto.getGenre());
        movie.setUrlImage("/" + uploadDir + "/" + fileName);
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Integer id, MovieRequestDto dto, String uploadDir) throws Exception {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie tidak ditemukan"));
        // Validasi semua field mandatory
        if (dto.getTitle() == null || dto.getTitle().isBlank()) throw new IllegalArgumentException("Title wajib diisi");
        if (dto.getRating() == null || dto.getRating().isBlank()) throw new IllegalArgumentException("Rating wajib diisi");
        if (dto.getDuration() == null) throw new IllegalArgumentException("Duration wajib diisi");
        if (dto.getGenre() == null || dto.getGenre().isBlank()) throw new IllegalArgumentException("Genre wajib diisi");
        if (dto.getUrlImage() == null || dto.getUrlImage().isEmpty()) throw new IllegalArgumentException("urlImage wajib diisi");
        // Title uniqueness (kecuali dirinya sendiri)
        Movie existing = movieRepository.findByTitleIgnoreCase(dto.getTitle());
        if (existing != null && !existing.getId().equals(id)) {
            throw new IllegalArgumentException("Movie dengan judul tersebut sudah ada");
        }
        // Update file jika ada file baru
        org.springframework.web.multipart.MultipartFile file = dto.getUrlImage();
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = java.util.UUID.randomUUID() + ext;
        java.nio.file.Path dirPath = java.nio.file.Paths.get(uploadDir).toAbsolutePath();
        if (!java.nio.file.Files.exists(dirPath)) {
            java.nio.file.Files.createDirectories(dirPath);
        }
        java.nio.file.Path filePath = dirPath.resolve(fileName);
        file.transferTo(filePath.toFile());
        // Update fields
        movie.setTitle(dto.getTitle());
        movie.setRating(dto.getRating());
        movie.setDuration(dto.getDuration().toString());
        movie.setGenre(dto.getGenre());
        movie.setUrlImage("/" + uploadDir + "/" + fileName);
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Integer id) throws Exception {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie tidak ditemukan"));
        movieRepository.delete(movie);
    }
    }

