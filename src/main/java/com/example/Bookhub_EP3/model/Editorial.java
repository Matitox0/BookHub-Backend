package com.example.Bookhub_EP3.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "editorial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_editorial")
    private Integer idEditorial;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;
}