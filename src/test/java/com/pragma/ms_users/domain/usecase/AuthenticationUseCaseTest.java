package com.pragma.ms_users.domain.usecase;

import com.pragma.ms_users.domain.spi.IAuthenticateServicePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationUseCaseTest {

    @Mock
    private IAuthenticateServicePort authenticateServicePort;

    @InjectMocks
    private AuthenticationUseCase authenticationUseCase;

    @Test
    @DisplayName("Debería delegar la autenticación al puerto y devolver el resultado")
    void authenticate_shouldDelegateToPortAndReturnResult() {
        // Arrange (Organizar)
        String email = "test@example.com";
        String password = "password123";
        String expectedToken = "a.very.long.and.secure.jwt.token";

        // 1. Programamos el mock para que devuelva el token esperado
        when(authenticateServicePort.authenticate(email, password)).thenReturn(expectedToken);

        // Act (Actuar)
        // 2. Llamamos al metodo del UseCase que estamos probando
        String actualToken = authenticationUseCase.authenticate(email, password);

        // Assert (Afirmar)
        // 3. Verificamos que el token devuelto es el que esperábamos
        assertEquals(expectedToken, actualToken);

        // 4. Verificamos que el metodo del puerto fue llamado exactamente una vez con los parámetros correctos
        verify(authenticateServicePort, times(1)).authenticate(email, password);
    }
}