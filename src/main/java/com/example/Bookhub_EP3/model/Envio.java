package com.example.Bookhub_EP3.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Integer idEnvio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_direccion", nullable = false)
    private Direccion direccion;

    @Column(name = "num_casa", length = 100)
    private String numCasa;

    private Integer costo;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
}