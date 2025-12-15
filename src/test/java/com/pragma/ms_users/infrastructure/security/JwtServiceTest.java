package com.pragma.ms_users.infrastructure.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    // Usaremos una clave secreta de prueba. Debe ser lo suficientemente larga para el algoritmo HS256.
    private final String secretKey = "dGhpcy1pcy1hLXZlcnktc2VjdXJlLWFuZC1sb25nLXNlY3JldC1rZXktZm9yLXBsYXpvbGV0YXM=";
    private final long jwtExpiration = 3600000; // 1 hora en milisegundos

    @BeforeEach
    void setUp() {
        // Inyectamos los valores de configuración en el servicio usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
    }

    @Test
    @DisplayName("Debería generar un token y extraer el username correctamente")
    void generateToken_and_extractUsername_shouldWorkCorrectly() {
        // Arrange
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                .build();

        // Act
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    @DisplayName("Debería generar un token con claims personalizados para CustomUserDetails")
    void generateToken_withCustomUserDetails_shouldContainExtraClaims() {
        // Arrange
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        CustomUserDetails customUserDetails = new CustomUserDetails(
                10L,
                "John Doe",
                "employee@example.com",
                "password",
                1L, // restaurantId
                (List<SimpleGrantedAuthority>) authorities
        );

        // Act
        String token = jwtService.generateToken(customUserDetails);
        Claims claims = jwtService.extractAllClaims(token);
        Map<String, Object> userClaims = claims.get("user", Map.class);
        List<String> roles = claims.get("roles", List.class);

        // Assert
        assertNotNull(userClaims);
        assertEquals(10, ((Number) userClaims.get("id")).longValue());
        assertEquals("John Doe", userClaims.get("name"));
        assertEquals(1, ((Number) userClaims.get("restaurantId")).longValue());

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals("ROLE_EMPLOYEE", roles.get(0));
    }

    @Test
    @DisplayName("Debería validar un token correctamente para el usuario correcto")
    void isTokenValid_whenTokenIsValidAndUserMatches_shouldReturnTrue() {
        // Arrange
        UserDetails userDetails = User.builder()
                .username("validuser@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Debería invalidar un token para un usuario diferente")
    void isTokenValid_whenUserDoesNotMatch_shouldReturnFalse() {
        // Arrange
        UserDetails originalUser = User.builder().username("original@user.com").password("p").authorities(Collections.emptyList()).build();
        UserDetails wrongUser = User.builder().username("wrong@user.com").password("p").authorities(Collections.emptyList()).build();
        String token = jwtService.generateToken(originalUser);

        // Act
        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debería extraer la fecha de expiración correctamente")
    void extractExpiration_shouldReturnCorrectDate() {
        // Arrange
        UserDetails userDetails = User.builder().username("user@example.com").password("p").authorities(Collections.emptyList()).build();
        long now = System.currentTimeMillis();
        String token = jwtService.generateToken(userDetails);

        // Act
        Date expirationDate = jwtService.extractExpiration(token);

        // Assert
        // Verificamos que la fecha de expiración es aproximadamente la esperada (ahora + duración)
        // Damos un margen de 5 segundos para la ejecución del test.
        long expectedExpirationTime = now + jwtExpiration;
        assertTrue(Math.abs(expectedExpirationTime - expirationDate.getTime()) < 5000);
    }
}