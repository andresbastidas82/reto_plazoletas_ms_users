package com.pragma.ms_users.application.handler.impl;

import com.pragma.ms_users.application.dto.AuthenticationRequest;
import com.pragma.ms_users.application.dto.AuthenticationResponse;
import com.pragma.ms_users.domain.api.IAuthenticationServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationHandlerTest {

    @Mock
    private IAuthenticationServicePort authenticationServicePort;

    @InjectMocks
    private AuthenticationHandler authenticationHandler;

    @Test
    void authenticate_shouldCallServiceAndReturnTokenInResponse() {
        // Arrange (Organizar)
        // 1. Crear la solicitud de entrada con datos de prueba.
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password123");
        String expectedToken = "a.very.secure.and.valid.jwt.token";

        // 2. Definir el comportamiento del mock: cuando se llame al servicio de autenticación
        // con el email y la contraseña correctos, debe devolver el token esperado.
        when(authenticationServicePort.authenticate(request.getEmail(), request.getPassword()))
                .thenReturn(expectedToken);

        // Act (Actuar)
        // 3. Llamar al metodo que estamos probando.
        AuthenticationResponse response = authenticationHandler.authenticate(request);

        // Assert (Afirmar)
        // 4. Verificar que la respuesta no es nula y que contiene el token correcto.
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());

        // 5. Verificar que el metodo del servicio fue llamado exactamente una vez
        // con los parámetros que le pasamos.
        verify(authenticationServicePort, times(1)).authenticate("test@example.com", "password123");
    }
}