package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "booking_sheats")
@Data
public class BookingSheat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "sheats_id")
    private Sheat sheats;

}
