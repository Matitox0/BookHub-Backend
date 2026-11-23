package com.example.Bookhub_EP3.controller;

import com.example.Bookhub_EP3.model.Editorial;
import com.example.Bookhub_EP3.service.EditorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/editoriales")
@RequiredArgsConstructor
@Tag(name = "Editoriales", description = "Gestión de editoriales de libros")
public class EditorialController {

    private final EditorialService editorialService;

    @GetMapping
    @Operation(summary = "Listar todas las editoriales")
    public ResponseEntity<List<Editorial>> listarTodas() {
        return ResponseEntity.ok(editorialService.listarTodas());
    }

    @PostMapping
    @Operation(summary = "Crear nueva editorial (Solo ADMIN)")
    public ResponseEntity<Editorial> crear(@RequestBody Editorial editorial) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editorialService.crear(editorial));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar editorial (Solo ADMIN)")
    public ResponseEntity<Editorial> actualizar(@PathVariable Integer id, @RequestBody Editorial editorial) {
        return ResponseEntity.ok(editorialService.actualizar(id, editorial));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar editorial (Solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        editorialService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}