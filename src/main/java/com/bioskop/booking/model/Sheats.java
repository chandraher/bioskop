package com.bioskop.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sheats")
@Data
public class Sheats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "theaters_id")
    private Theater theater;

    @Column(name = "row_position", nullable = false)
    private Integer row;

    @Column(name = "column_position", nullable = false)
    private Integer column;

    @Column(name = "flag", nullable = false)
    private String flag;
}
