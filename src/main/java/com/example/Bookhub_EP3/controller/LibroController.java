package com.example.Bookhub_EP3.controller;

import com.example.Bookhub_EP3.DTO.LibroRequestDTO;
import com.example.Bookhub_EP3.model.Libro;
import com.example.Bookhub_EP3.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
@Tag(name = "Libros", description = "Gestión del catálogo de libros (lectura pública, escritura ADMIN)")
public class LibroController {

    private final LibroService libroService;

    @GetMapping
    @Operation(summary = "Listar todos los libros disponibles")
    public ResponseEntity<List<Libro>> listarTodos() {
        return ResponseEntity.ok(libroService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener el detalle de un libro por su ID")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro (Solo ADMIN)")
    public ResponseEntity<Libro> crear(@Valid @RequestBody LibroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro existente (Solo ADMIN)")
    public ResponseEntity<Libro> actualizar(@PathVariable Integer id, @Valid @RequestBody LibroRequestDTO request) {
        return ResponseEntity.ok(libroService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro del catálogo (Solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
