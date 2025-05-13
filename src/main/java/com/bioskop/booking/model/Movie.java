package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "movie")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "rating")
    private String rating;

    @Column(name = "duration")
    private String duration;

    @Column(name = "genre")
    private String genre;

}
