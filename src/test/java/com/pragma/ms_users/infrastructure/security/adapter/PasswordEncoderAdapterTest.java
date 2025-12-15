package com.pragma.ms_users.infrastructure.security.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderAdapter passwordEncoderAdapter;

    @Test
    @DisplayName("Debería llamar al método encode del PasswordEncoder y devolver el resultado")
    void passwordEncrypt_shouldCallEncoderAndReturnResult() {
        // Arrange
        String plainPassword = "mySecretPassword123";
        String expectedEncryptedPassword = "aVeryStronglyEncryptedPassword!@#$%^&*()";

        // 1. Configurar el mock para que devuelva la contraseña encriptada esperada
        //    cuando se llame a su metodo encode.
        when(passwordEncoder.encode(plainPassword)).thenReturn(expectedEncryptedPassword);

        // Act
        // 2. Llamar al metodo del adaptador que estamos probando.
        String actualEncryptedPassword = passwordEncoderAdapter.passwordEncrypt(plainPassword);

        // Assert
        // 3. Verificar que el resultado devuelto por el adaptador es el que esperamos.
        assertEquals(expectedEncryptedPassword, actualEncryptedPassword);

        // 4. Verificar que el metodo `encode` del mock fue llamado exactamente una vez
        //    con la contraseña en texto plano correcta.
        verify(passwordEncoder, times(1)).encode(plainPassword);
    }
}