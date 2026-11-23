package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.DTO.UsuarioRequestDTO;
import com.example.Bookhub_EP3.model.Rol;
import com.example.Bookhub_EP3.model.Usuario;
import com.example.Bookhub_EP3.repository.RolRepository;
import com.example.Bookhub_EP3.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public Usuario crearUsuario(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
        if (request.getClave() == null || request.getClave().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria para crear un usuario");
        }

        Rol rol = rolRepository.findByNombre(request.getRol().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Rol no válido: " + request.getRol()));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .clave(passwordEncoder.encode(request.getClave()))
                .generoFavorito(request.getGeneroFavorito())
                .rol(rol)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarUsuario(Integer id, UsuarioRequestDTO request) {
        Usuario usuario = obtenerPorId(id);

        if (!usuario.getEmail().equalsIgnoreCase(request.getEmail())
                && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso por otro usuario");
        }

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setGeneroFavorito(request.getGeneroFavorito());

        if (request.getRol() != null && !request.getRol().isBlank()) {
            Rol rol = rolRepository.findByNombre(request.getRol().toLowerCase())
                    .orElseThrow(() -> new RuntimeException("Rol no válido: " + request.getRol()));
            usuario.setRol(rol);
        }

        if (request.getClave() != null && !request.getClave().isBlank()) {
            usuario.setClave(passwordEncoder.encode(request.getClave()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}