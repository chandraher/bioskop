package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "booking")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "shows_id")
    private Shows shows;

    @ManyToOne
    @JoinColumn(name = "sheats_id")
    private Sheats sheats;


    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;


}
