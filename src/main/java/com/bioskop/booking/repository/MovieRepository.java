package com.bioskop.booking.repository;

import com.bioskop.booking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
    boolean existsByTitleIgnoreCase(String title);
    Movie findByTitleIgnoreCase(String title);
    // JpaSpecificationExecutor digunakan untuk pencarian dinamis
}
