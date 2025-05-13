package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "booking_sheats")
@Data
public class BookingSheats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "sheats_id")
    private Sheats sheats;

    @Column(name = "price")
    private Integer price;
}
