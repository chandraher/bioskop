package com.bioskop.booking.repository;

import com.bioskop.booking.model.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaHallRepository extends JpaRepository<CinemaHall, Integer>, JpaSpecificationExecutor<CinemaHall> {
}
