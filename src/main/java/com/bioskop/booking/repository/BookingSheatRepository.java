package com.bioskop.booking.repository;

import com.bioskop.booking.model.BookingSheat;
import com.bioskop.booking.model.Sheat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingSheatRepository extends JpaRepository<BookingSheat, Integer> {
    @Query("SELECT bs.sheats FROM BookingSheat bs WHERE bs.booking.shows.id = :showId")
    List<Sheat> findBookedSheatsByShowId(@Param("showId") Integer showId);
}
