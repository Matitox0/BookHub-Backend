package com.example.Bookhub_EP3.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}
