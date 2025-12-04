package com.pragma.ms_users.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Establecemos el código de estado 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Creamos el cuerpo de la respuesta de error personalizado
        Map<String, List<String>> error = new HashMap<>();
        // Usamos el mensaje de la excepción original, que suele ser "Bad credentials" o similar
        error.put("errors", List.of(authException.getMessage()));

        // Escribimos la respuesta JSON
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
