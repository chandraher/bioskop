package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "theater")
@Data
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cinema_hall_id")
    private CinemaHall cinemaHall;

    @Column(name = "name")
    private String name;

    @Column(name = "max_row")
    private Integer maxRow;

    @Column(name = "max_column")
    private Integer maxColumn;

}
