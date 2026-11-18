package com.example.Bookhub_EP3.controller;

import com.example.Bookhub_EP3.DTO.UsuarioRequestDTO;
import com.example.Bookhub_EP3.model.Usuario;
import com.example.Bookhub_EP3.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Administración de usuarios y roles (Solo ADMIN)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios registrados (Solo ADMIN)")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID (Solo ADMIN)")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear usuario con rol específico: admin, vendedor o cliente (Solo ADMIN)")
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos o rol de un usuario (Solo ADMIN)")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario (Solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
