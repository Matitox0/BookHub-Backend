package com.example.Bookhub_EP3.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    private String nombre;
    private String email;
    private String clave;
    private String generoFavorito;
    private String rol;
}
