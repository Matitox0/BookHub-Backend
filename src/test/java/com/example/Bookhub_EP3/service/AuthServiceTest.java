package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.DTO.AuthResponseDTO;
import com.example.Bookhub_EP3.DTO.LoginRequestDTO;
import com.example.Bookhub_EP3.DTO.RegisterRequestDTO;
import com.example.Bookhub_EP3.security.JwtService;
import com.example.Bookhub_EP3.model.Rol;
import com.example.Bookhub_EP3.model.Usuario;
import com.example.Bookhub_EP3.repository.RolRepository;
import com.example.Bookhub_EP3.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_CuandoDatosSonValidos_RetornaTokenYRolCliente() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Ana Pérez", "ana@bookhub.cl", "123456", "Fantasía");
        Rol rolCliente = Rol.builder().idRol(1).nombre("cliente").build();

        when(usuarioRepository.existsByEmail("ana@bookhub.cl")).thenReturn(false);
        when(rolRepository.findByNombre("cliente")).thenReturn(Optional.of(rolCliente));
        when(passwordEncoder.encode("123456")).thenReturn("clave_encriptada_bcrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setIdUsuario(15);
            return u;
        });
        when(jwtService.generateToken(anyMap(), any(Usuario.class))).thenReturn("jwt.token.simulado");

        // Act
        AuthResponseDTO response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt.token.simulado", response.getToken());
        assertEquals("cliente", response.getRol());
        assertEquals("ana@bookhub.cl", response.getEmail());
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void register_CuandoEmailYaExiste_LanzaExcepcion() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Ana", "duplicado@bookhub.cl", "123456", "Terror");
        when(usuarioRepository.existsByEmail("duplicado@bookhub.cl")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void login_CuandoCredencialesSonCorrectas_RetornaAuthResponse() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("admin@bookhub.cl", "admin123");
        Rol rolAdmin = Rol.builder().idRol(3).nombre("admin").build();
        Usuario admin = Usuario.builder()
                .idUsuario(1)
                .nombre("Admin BookHub")
                .email("admin@bookhub.cl")
                .clave("hash")
                .rol(rolAdmin)
                .build();

        when(usuarioRepository.findByEmail("admin@bookhub.cl")).thenReturn(Optional.of(admin));
        when(jwtService.generateToken(anyMap(), eq(admin))).thenReturn("token.admin.jwt");

        // Act
        AuthResponseDTO response = authService.login(request);

        // Assert
        assertEquals("token.admin.jwt", response.getToken());
        assertEquals("admin", response.getRol());
        verify(authenticationManager).authenticate(any());
    }
}