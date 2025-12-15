package com.pragma.ms_users.infrastructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        // Limpiar el contexto de seguridad antes de cada test para asegurar el aislamiento
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Limpiar de nuevo por si algún test deja estado
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Debería continuar la cadena de filtros si no hay cabecera de autorización")
    void doFilterInternal_whenNoAuthorizationHeader_thenContinuesChain() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        // Verificar que el contexto de seguridad sigue vacío
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // Verificar que se llamó al siguiente filtro en la cadena
        verify(filterChain, times(1)).doFilter(request, response);
        // Verificar que no se interactuó con los servicios de JWT o UserDetails
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    @DisplayName("Debería continuar la cadena si la cabecera no empieza con 'Bearer '")
    void doFilterInternal_whenHeaderNotBearer_thenContinuesChain() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNzd29yZA==");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    @DisplayName("Debería autenticar al usuario si el token es válido")
    void doFilterInternal_whenTokenIsValid_thenAuthenticatesUser() throws ServletException, IOException {
        // Arrange
        final String token = "valid.jwt.token";
        final String authHeader = "Bearer " + token;
        final String userEmail = "user@example.com";
        final UserDetails userDetails = new User(userEmail, "password", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        // Verificar que el contexto de seguridad ahora contiene un objeto de autenticación
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userEmail, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("No debería autenticar si el token es inválido")
    void doFilterInternal_whenTokenIsInvalid_thenDoesNotAuthenticate() throws ServletException, IOException {
        // Arrange
        final String token = "invalid.jwt.token";
        final String authHeader = "Bearer " + token;
        final String userEmail = "user@example.com";
        final UserDetails userDetails = new User(userEmail, "password", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        // Simular que el servicio considera el token inválido
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("No debería autenticar si el token está expirado")
    void doFilterInternal_whenTokenIsExpired_thenDoesNotAuthenticate() throws ServletException, IOException {
        // Arrange
        final String token = "expired.jwt.token";
        final String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        // Simular que el servicio lanza una excepción de token expirado
        when(jwtService.extractUsername(token)).thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
        // Verificar que no se intentó cargar el usuario si la extracción del token falló
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("No debería autenticar si el token está malformado")
    void doFilterInternal_whenTokenIsMalformed_thenDoesNotAuthenticate() throws ServletException, IOException {
        // Arrange
        final String token = "malformed-token";
        final String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        // Simular que el servicio lanza una excepción de token malformado
        when(jwtService.extractUsername(token)).thenThrow(new MalformedJwtException("Invalid token"));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}