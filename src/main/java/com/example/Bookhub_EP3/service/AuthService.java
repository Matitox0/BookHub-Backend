package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.DTO.AuthResponseDTO;
import com.example.Bookhub_EP3.DTO.LoginRequestDTO;
import com.example.Bookhub_EP3.DTO.RegisterRequestDTO;
import com.example.Bookhub_EP3.security.JwtService;
import com.example.Bookhub_EP3.model.Rol;
import com.example.Bookhub_EP3.model.Usuario;
import com.example.Bookhub_EP3.repository.RolRepository;
import com.example.Bookhub_EP3.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Rol rolCliente = rolRepository.findByNombre("cliente")
                .orElseThrow(() -> new RuntimeException("Rol 'cliente' no encontrado en la base de datos"));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .clave(passwordEncoder.encode(request.getClave()))
                .generoFavorito(request.getGeneroFavorito())
                .rol(rolCliente)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        String jwtToken = generarTokenConClaims(guardado);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .idUsuario(guardado.getIdUsuario())
                .nombre(guardado.getNombre())
                .email(guardado.getEmail())
                .rol(guardado.getRol().getNombre())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getClave()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwtToken = generarTokenConClaims(usuario);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().getNombre())
                .build();
    }

    private String generarTokenConClaims(Usuario usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", usuario.getRol().getNombre());
        extraClaims.put("nombre", usuario.getNombre());
        extraClaims.put("idUsuario", usuario.getIdUsuario());
        return jwtService.generateToken(extraClaims, usuario);
    }
}