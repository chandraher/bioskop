package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "shows")
@Data
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_show")
    private Date dateShow;

    @Temporal(TemporalType.TIME)
    @Column(name = "end_time")
    private Date endTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "status")
    private String status; //open, proggres, closed

    @Column(name = "price")
    private BigDecimal price;

    //add price for weekend / holiday

}
