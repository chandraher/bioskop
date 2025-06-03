package com.bioskop.booking.repository;

import com.bioskop.booking.model.Sheat;
import com.bioskop.booking.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SheatRepository extends JpaRepository<Sheat, Integer> {
    List<Sheat> findByTheater(Theater theater);
}
