package com.example.Bookhub_EP3.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    @NotNull(message = "El método de pago es obligatorio")
    private Integer idMetodoPago;

    private String clienteNombre;
    private String clienteCorreo;
    private String calleNumero;
    private String departamentoVilla;
    private Integer idComuna;
    private String numCasa;
    private Integer costoEnvio;

    @Valid
    @NotEmpty(message = "El pedido debe incluir al menos un detalle")
    private List<DetallePedidoRequestDTO> detalles;
}
