package com.example.Bookhub_EP3.controller;

import com.example.Bookhub_EP3.DTO.PedidoRequestDTO;
import com.example.Bookhub_EP3.model.Pedido;
import com.example.Bookhub_EP3.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos / Órdenes", description = "Gestión de compras, historial de clientes y consulta de órdenes")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar todas las órdenes (VENDEDOR y ADMIN)")
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver detalle de una orden por ID (VENDEDOR y ADMIN)")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @GetMapping("/mis-pedidos")
    @Operation(summary = "Obtener historial de compras del cliente autenticado")
    public ResponseEntity<List<Pedido>> listarMisPedidos(Authentication authentication) {
        String emailUsuario = authentication.getName();
        return ResponseEntity.ok(pedidoService.listarPorUsuarioEmail(emailUsuario));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva compra / boleta (CLIENTE y ADMIN)")
    public ResponseEntity<Pedido> crearPedido(
            @Valid @RequestBody PedidoRequestDTO request,
            Authentication authentication) {
        String emailUsuario = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(emailUsuario, request));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de una orden (Solo ADMIN)")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam Integer idEstado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, idEstado));
    }
}
