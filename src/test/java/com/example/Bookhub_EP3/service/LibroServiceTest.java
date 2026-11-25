package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.DTO.LibroRequestDTO;
import com.example.Bookhub_EP3.model.Categoria;
import com.example.Bookhub_EP3.model.Editorial;
import com.example.Bookhub_EP3.model.Libro;
import com.example.Bookhub_EP3.repository.CategoriaRepository;
import com.example.Bookhub_EP3.repository.EditorialRepository;
import com.example.Bookhub_EP3.repository.LibroRepository;
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
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;
    @Mock
    private EditorialRepository editorialRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private LibroService libroService;

    @Test
    void crearLibro_CuandoEditorialExiste_GuardaYRetornaLibro() {
        // Arrange
        Editorial editorial = Editorial.builder().idEditorial(1).nombre("Planeta").build();
        Categoria categoria = Categoria.builder().idCategorias(2).nombre("Ciencia Ficción").build();

        LibroRequestDTO request = new LibroRequestDTO(
                "Dune",
                "Frank Herbert",
                24990,
                12,
                "Clásico de ciencia ficción en el planeta Arrakis",
                1,
                List.of(2),
                List.of("https://ejemplo.com/dune.jpg")
        );

        when(editorialRepository.findById(1)).thenReturn(Optional.of(editorial));
        when(categoriaRepository.findAllById(List.of(2))).thenReturn(List.of(categoria));
        when(libroRepository.save(any(Libro.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Libro creado = libroService.crear(request);

        // Assert
        assertNotNull(creado);
        assertEquals("Dune", creado.getTitulo());
        assertEquals("Planeta", creado.getEditorial().getNombre());
        assertEquals(1, creado.getCategorias().size());
        assertEquals(1, creado.getImagenes().size());
    }

    @Test
    void obtenerPorId_CuandoNoExiste_LanzaExcepcion() {
        // Arrange
        when(libroRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> libroService.obtenerPorId(999));
        assertTrue(ex.getMessage().contains("Libro no encontrado"));
    }
}