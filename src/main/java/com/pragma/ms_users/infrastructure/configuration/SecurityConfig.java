package com.pragma.ms_users.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf(AbstractHttpConfigurer::disable) // deshabilitar CSRF para clientes externos como Postman
        .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // permitir TODAS las rutas
        )
        .httpBasic(AbstractHttpConfigurer::disable) // deshabilitar autenticación básica
        .formLogin(AbstractHttpConfigurer::disable); // deshabilitar formulario de login

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
