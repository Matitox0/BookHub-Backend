package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.DTO.DetallePedidoRequestDTO;
import com.example.Bookhub_EP3.DTO.PedidoRequestDTO;
import com.example.Bookhub_EP3.model.*;
import com.example.Bookhub_EP3.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private LibroRepository libroRepository;
    @Mock
    private MetodoPagoRepository metodoPagoRepository;
    @Mock
    private EstadoRepository estadoRepository;
    @Mock
    private ComunaRepository comunaRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario usuarioMock;
    private MetodoPago metodoPagoMock;
    private Estado estadoMock;
    private Libro libroMock;

    @BeforeEach
    void setUp() {
        usuarioMock = Usuario.builder()
                .idUsuario(1)
                .nombre("Matías")
                .email("cliente@bookhub.cl")
                .build();

        metodoPagoMock = MetodoPago.builder()
                .idMetodoPago(1)
                .nombre("Tarjeta de Crédito")
                .build();

        estadoMock = Estado.builder()
                .idEstado(2)
                .nombre("Pagado")
                .build();

        libroMock = Libro.builder()
                .idLibros(10)
                .titulo("El Señor de los Anillos")
                .precio(20000)
                .stock(5)
                .build();
    }

    @Test
    void crearPedido_CuandoHayStock_DescuentaStockYCalculaTotal() {
        // Arrange
        DetallePedidoRequestDTO item = new DetallePedidoRequestDTO(10, 2);
        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setIdMetodoPago(1);
        request.setDetalles(List.of(item));

        when(usuarioRepository.findByEmail("cliente@bookhub.cl")).thenReturn(Optional.of(usuarioMock));
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPagoMock));
        when(estadoRepository.findByNombre("Pagado")).thenReturn(Optional.of(estadoMock));
        when(libroRepository.findById(10)).thenReturn(Optional.of(libroMock));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pedido resultado = pedidoService.crearPedido("cliente@bookhub.cl", request);

        // Assert
        assertNotNull(resultado);
        assertEquals(40000, resultado.getTotal()); // 2 libros x $20.000
        assertEquals(3, libroMock.getStock());     // 5 iniciales - 2 comprados = 3
        assertEquals(1, resultado.getDetalles().size());
        verify(libroRepository, times(1)).save(libroMock);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void crearPedido_CuandoStockEsInsuficiente_LanzaExcepcionYNoGuarda() {
        // Arrange
        DetallePedidoRequestDTO item = new DetallePedidoRequestDTO(10, 10); // Pide 10, solo hay 5
        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setIdMetodoPago(1);
        request.setDetalles(List.of(item));

        when(usuarioRepository.findByEmail("cliente@bookhub.cl")).thenReturn(Optional.of(usuarioMock));
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPagoMock));
        when(estadoRepository.findByNombre("Pagado")).thenReturn(Optional.of(estadoMock));
        when(libroRepository.findById(10)).thenReturn(Optional.of(libroMock));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                pedidoService.crearPedido("cliente@bookhub.cl", request)
        );

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        assertEquals(5, libroMock.getStock()); // El stock no debe alterarse
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}