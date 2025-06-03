package com.bioskop.booking.repository;

import com.bioskop.booking.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {
    @org.springframework.data.jpa.repository.Query("""
        SELECT s FROM Show s 
        WHERE (:dateShow IS NULL OR s.dateShow = :dateShow)
          AND (:time IS NULL OR FUNCTION('TIME', s.startTime) = :time OR FUNCTION('TIME', s.endTime) = :time)
          AND (:cinemaHallId IS NULL OR s.theater.cinemaHall.id = :cinemaHallId)
          AND (:theaterId IS NULL OR s.theater.id = :theaterId)
    """)
    java.util.List<Show> findShowsByFilter(
        @org.springframework.data.repository.query.Param("dateShow") java.util.Date dateShow,
        @org.springframework.data.repository.query.Param("time") String time,
        @org.springframework.data.repository.query.Param("cinemaHallId") Integer cinemaHallId,
        @org.springframework.data.repository.query.Param("theaterId") Integer theaterId
    );
}
