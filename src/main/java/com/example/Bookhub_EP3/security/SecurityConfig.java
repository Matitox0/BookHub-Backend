package com.example.Bookhub_EP3.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. Endpoints públicos (Login, Registro y Swagger)
                .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // 2. Catálogo y tablas auxiliares públicas (Libros, Categorías, Editoriales, Regiones/Comunas/Pagos)
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/libros/**",
                        "/api/v1/categorias/**",
                        "/api/v1/editoriales/**",
                        "/api/v1/catalogos/**"
                ).permitAll()

                // 3. Compras del cliente (DEBE ir antes de /api/v1/pedidos/{id})
                .requestMatchers(HttpMethod.GET, "/api/v1/pedidos/mis-pedidos/**").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/pedidos").hasAnyRole("CLIENTE", "ADMIN")

                // 4. Órdenes / Pedidos: Vendedor y Admin pueden listar y ver detalle
                .requestMatchers(HttpMethod.GET, "/api/v1/pedidos", "/api/v1/pedidos/{id}").hasAnyRole("VENDEDOR", "ADMIN")

                // 5. Administración total: solo ADMIN modifica catálogo, cambia estados de pedidos y gestiona usuarios
                .requestMatchers("/api/v1/libros/**", "/api/v1/categorias/**", "/api/v1/editoriales/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/pedidos/**", "/api/v1/usuarios/**", "/api/v1/roles/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
