package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.DTO.DetallePedidoRequestDTO;
import com.example.Bookhub_EP3.DTO.PedidoRequestDTO;
import com.example.Bookhub_EP3.model.*;
import com.example.Bookhub_EP3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final EstadoRepository estadoRepository;
    private final ComunaRepository comunaRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByFechaDesc();
    }

    public Pedido obtenerPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    public List<Pedido> listarPorUsuarioEmail(String email) {
        return pedidoRepository.findByUsuarioEmailOrderByFechaDesc(email);
    }

    @Transactional
    public Pedido crearPedido(String emailUsuario, PedidoRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + emailUsuario));

        MetodoPago metodoPago = metodoPagoRepository.findById(request.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException("Método de pago no válido"));

        Estado estadoInicial = estadoRepository.findByNombre("Pagado")
                .orElseGet(() -> estadoRepository.findById(1)
                        .orElseThrow(() -> new RuntimeException("Estado inicial no configurado en BD")));

        Pedido pedido = Pedido.builder()
                .nroOrden("BH-" + System.currentTimeMillis())
                .usuario(usuario)
                .metodoPago(metodoPago)
                .estado(estadoInicial)
                .clienteNombre(request.getClienteNombre() != null ? request.getClienteNombre() : usuario.getNombre())
                .clienteCorreo(request.getClienteCorreo() != null ? request.getClienteCorreo() : usuario.getEmail())
                .fecha(LocalDateTime.now())
                .build();

        // Registrar dirección de envío si viene en la solicitud
        if (request.getCalleNumero() != null && !request.getCalleNumero().isBlank() && request.getIdComuna() != null) {
            Comuna comuna = comunaRepository.findById(request.getIdComuna())
                    .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

            Direccion direccion = Direccion.builder()
                    .calleNumero(request.getCalleNumero())
                    .departamentoVilla(request.getDepartamentoVilla())
                    .comuna(comuna)
                    .build();

            Envio envio = Envio.builder()
                    .direccion(direccion)
                    .numCasa(request.getNumCasa())
                    .costo(request.getCostoEnvio() != null ? request.getCostoEnvio() : 0)
                    .build();

            pedido.setEnvio(envio);
        }

        List<DetallePedido> detalles = new ArrayList<>();
        int totalPedido = (pedido.getEnvio() != null && pedido.getEnvio().getCosto() != null)
                ? pedido.getEnvio().getCosto()
                : 0;

        for (DetallePedidoRequestDTO item : request.getDetalles()) {
            Libro libro = libroRepository.findById(item.getIdLibro())
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + item.getIdLibro()));

            if (libro.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el libro: " + libro.getTitulo());
            }

            // Descontar inventario
            libro.setStock(libro.getStock() - item.getCantidad());
            libroRepository.save(libro);

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .libro(libro)
                    .titulo(libro.getTitulo())
                    .cantidad(item.getCantidad())
                    .precio(libro.getPrecio())
                    .build();

            totalPedido += libro.getPrecio() * item.getCantidad();
            detalles.add(detalle);
        }

        pedido.setTotal(totalPedido);
        pedido.setDetalles(detalles);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarEstado(Integer idPedido, Integer idEstado) {
        Pedido pedido = obtenerPorId(idPedido);
        Estado nuevoEstado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + idEstado));

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}
