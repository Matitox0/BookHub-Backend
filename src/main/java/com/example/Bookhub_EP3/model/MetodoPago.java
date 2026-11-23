package com.example.Bookhub_EP3.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "metodo_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
}
