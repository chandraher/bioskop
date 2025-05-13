package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "shows")
@Data
public class Shows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "startTime")
    private Date startTime;

    @Temporal(TemporalType.TIME)
    private Date endTime;

    @Column(name = "status")
    private String status;
}
