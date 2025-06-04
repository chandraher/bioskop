package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "booking_sheats")
@Data
public class BookingSheat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_sheats_seq_gen")
    @SequenceGenerator(
        name = "booking_sheats_seq_gen",
        sequenceName = "booking_sheats_seq",
        allocationSize = 1
    )
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "sheats_id")
    private Sheat sheats;

}
