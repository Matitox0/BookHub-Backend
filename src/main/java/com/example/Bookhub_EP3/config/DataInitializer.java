package com.example.Bookhub_EP3.config;

import com.example.Bookhub_EP3.model.Estado;
import com.example.Bookhub_EP3.model.MetodoPago;
import com.example.Bookhub_EP3.model.Rol;
import com.example.Bookhub_EP3.model.Usuario;
import com.example.Bookhub_EP3.repository.EstadoRepository;
import com.example.Bookhub_EP3.repository.MetodoPagoRepository;
import com.example.Bookhub_EP3.repository.RolRepository;
import com.example.Bookhub_EP3.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Rol rolCliente = crearRolSiNoExiste("cliente");
        Rol rolVendedor = crearRolSiNoExiste("vendedor");
        Rol rolAdmin = crearRolSiNoExiste("admin");

        for (String nombreEstado : List.of("Pendiente", "Pagado", "Enviado", "Entregado", "Cancelado")) {
            if (estadoRepository.findByNombre(nombreEstado).isEmpty()) {
                estadoRepository.save(Estado.builder().nombre(nombreEstado).build());
            }
        }

        for (String nombrePago : List.of("Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia Bancaria")) {
            if (metodoPagoRepository.findByNombre(nombrePago).isEmpty()) {
                metodoPagoRepository.save(MetodoPago.builder().nombre(nombrePago).build());
            }
        }

        crearUsuarioSiNoExiste("Admin BookHub", "admin@bookhub.cl", "123456", rolAdmin);
        crearUsuarioSiNoExiste("Vendedor BookHub", "vendedor@bookhub.cl", "123456", rolVendedor);
        crearUsuarioSiNoExiste("Cliente BookHub", "cliente@bookhub.cl", "123456", rolCliente);
    }

    private Rol crearRolSiNoExiste(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre(nombre).build()));
    }

    private void crearUsuarioSiNoExiste(String nombre, String email, String clavePlana, Rol rol) {
        if (!usuarioRepository.existsByEmail(email)) {
            usuarioRepository.save(Usuario.builder()
                    .nombre(nombre)
                    .email(email)
                    .clave(passwordEncoder.encode(clavePlana))
                    .rol(rol)
                    .build());
        }
    }
}