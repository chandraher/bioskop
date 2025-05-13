package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "schedule")
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "startTime")
    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Column(name = "endTime")
    @Temporal(TemporalType.TIME)
    private Date endTime;
}
