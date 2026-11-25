
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

                // 1. Página principal y documentación pública
                .requestMatchers(
                    "/",
                    "/error",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // 2. Login y registro
                .requestMatchers("/api/v1/auth/**").permitAll()

                // 3. Catálogo público
                .requestMatchers(HttpMethod.GET,
                    "/api/v1/libros/**",
                    "/api/v1/categorias/**",
                    "/api/v1/editoriales/**",
                    "/api/v1/catalogos/**"
                ).permitAll()

                // 4. Pedidos del cliente
                .requestMatchers(HttpMethod.GET,
                    "/api/v1/pedidos/mis-pedidos/**"
                ).hasAnyRole("CLIENTE", "ADMIN")

                .requestMatchers(HttpMethod.POST,
                    "/api/v1/pedidos"
                ).hasAnyRole("CLIENTE", "ADMIN")

                // 5. Consulta de pedidos para vendedor y administrador
                .requestMatchers(HttpMethod.GET,
                    "/api/v1/pedidos",
                    "/api/v1/pedidos/{id}"
                ).hasAnyRole("VENDEDOR", "ADMIN")

                // 6. Administración del catálogo
                .requestMatchers(
                    "/api/v1/libros/**",
                    "/api/v1/categorias/**",
                    "/api/v1/editoriales/**"
                ).hasRole("ADMIN")

                // 7. Administración de usuarios y pedidos
                .requestMatchers(
                    "/api/v1/pedidos/**",
                    "/api/v1/usuarios/**",
                    "/api/v1/roles/**"
                ).hasRole("ADMIN")

                // 8. Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
