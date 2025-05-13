package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cinema_hall")
@Data
public class CinemaHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;
}
