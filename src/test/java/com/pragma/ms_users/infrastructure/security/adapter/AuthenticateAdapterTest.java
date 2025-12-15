package com.pragma.ms_users.infrastructure.security.adapter;

import com.pragma.ms_users.infrastructure.security.JwtService;
import com.pragma.ms_users.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateAdapterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticateAdapter authenticateAdapter;

    @Test
    @DisplayName("Debería devolver un token JWT cuando las credenciales son válidas")
    void authenticate_whenCredentialsAreValid_shouldReturnJwtToken() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String expectedToken = "a.very.secure.jwt.token";

        // 1. Simular que el AuthenticationManager valida las credenciales sin lanzar excepción.
        // El método authenticate devuelve un objeto Authentication, pero para este test no necesitamos usarlo.
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // O un mock de Authentication si fuera necesario

        // 2. Simular que el UserDetailsService encuentra al usuario.
        UserDetails userDetails = new User(email, password, Collections.emptyList());
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // 3. Simular que el JwtService genera un token para ese usuario.
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        // Act
        String actualToken = authenticateAdapter.authenticate(email, password);

        // Assert
        // 4. Verificar que el token devuelto es el esperado.
        assertEquals(expectedToken, actualToken);

        // 5. Verificar que cada dependencia fue llamada exactamente una vez.
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(email);
        verify(jwtService, times(1)).generateToken(userDetails);
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando las credenciales son inválidas")
    void authenticate_whenCredentialsAreInvalid_shouldThrowException() {
        // Arrange
        String email = "wrong@example.com";
        String password = "wrongpassword";

        // 1. Simular que el AuthenticationManager rechaza las credenciales.
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        // 2. Verificar que se lanza la excepción esperada.
        assertThrows(BadCredentialsException.class, () -> {
            authenticateAdapter.authenticate(email, password);
        });

        // 3. Verificar que el flujo se detuvo y no se intentó cargar el usuario ni generar un token.
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any(UserDetails.class));
    }
}