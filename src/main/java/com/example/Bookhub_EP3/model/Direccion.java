package com.example.Bookhub_EP3.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;

    @Column(name = "calle_numero", nullable = false, length = 255)
    private String calleNumero;

    @Column(name = "departamento_villa", length = 100)
    private String departamentoVilla;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_comuna", nullable = false)
    private Comuna comuna;
}