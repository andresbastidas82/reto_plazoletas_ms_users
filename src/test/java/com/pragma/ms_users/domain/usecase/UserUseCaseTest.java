package com.pragma.ms_users.domain.usecase;

import com.pragma.ms_users.domain.exception.AdultException;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import com.pragma.ms_users.domain.spi.IPasswordEncoderServicePort;
import com.pragma.ms_users.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderServicePort passwordEncoderServicePort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .password("plainPassword123")
                .birthDate(LocalDate.of(1990, 1, 1)) // Adult user
                .build();
    }

    @Test
    @DisplayName("Debería guardar un usuario no propietario sin verificar la edad")
    void saveUser_whenRoleIsNotOwner_shouldSaveSuccessfully() {
        // Arrange
        String encryptedPassword = "encryptedPassword!@#";
        when(passwordEncoderServicePort.passwordEncrypt(user.getPassword())).thenReturn(encryptedPassword);
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        // Act
        userUseCase.saveUser(user, RoleEnum.ROLE_CUSTOMER);

        // Assert
        // Capturamos el objeto User que se pasa al metodo de persistencia
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort, times(1)).saveUser(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        // Verificamos que la contraseña fue encriptada y el rol fue asignado
        assertEquals(encryptedPassword, capturedUser.getPassword());
        assertEquals(RoleEnum.ROLE_CUSTOMER, capturedUser.getRole());

        // Verificamos que los mocks fueron llamados
        verify(passwordEncoderServicePort, times(1)).passwordEncrypt("plainPassword123");
    }

    @Test
    @DisplayName("Debería guardar un propietario si es mayor de edad")
    void saveUser_whenOwnerIsAdult_shouldSaveSuccessfully() {
        // Arrange
        String encryptedPassword = "encryptedPassword!@#";
        when(passwordEncoderServicePort.passwordEncrypt(user.getPassword())).thenReturn(encryptedPassword);
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        // Act & Assert
        // Usamos assertDoesNotThrow para asegurarnos de que no se lanza la excepción
        assertDoesNotThrow(() -> userUseCase.saveUser(user, RoleEnum.ROLE_OWNER));

        // Verificamos que el flujo de guardado continuó
        verify(passwordEncoderServicePort, times(1)).passwordEncrypt("plainPassword123");
        verify(userPersistencePort, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Debería lanzar AdultException si un propietario es menor de edad")
    void saveUser_whenOwnerIsUnderage_shouldThrowAdultException() {
        // Arrange
        // Creamos un usuario que es menor de 18 años
        user.setBirthDate(LocalDate.now().minusYears(17));

        // Act & Assert
        // Verificamos que se lanza la excepción correcta
        assertThrows(AdultException.class, () -> {
            userUseCase.saveUser(user, RoleEnum.ROLE_OWNER);
        });

        // Verificamos que el flujo se detuvo y no se intentó encriptar ni guardar
        verify(passwordEncoderServicePort, never()).passwordEncrypt(anyString());
        verify(userPersistencePort, never()).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Debería obtener un usuario por su ID")
    void getUserById_shouldReturnUserFromPersistence() {
        // Arrange
        Long userId = 1L;
        when(userPersistencePort.getUserById(userId)).thenReturn(user);

        // Act
        User foundUser = userUseCase.getUserById(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        assertEquals("John", foundUser.getName());

        verify(userPersistencePort, times(1)).getUserById(userId);
    }
}