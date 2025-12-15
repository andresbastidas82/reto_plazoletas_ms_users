package com.pragma.ms_users.infrastructure.out.jpa.adapter;

import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import com.pragma.ms_users.infrastructure.exception.UserAlreadyExistsException;
import com.pragma.ms_users.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.ms_users.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.pragma.ms_users.infrastructure.out.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    private User userDomain;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userDomain = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encryptedPass")
                .role(RoleEnum.ROLE_CUSTOMER)
                .build();

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("encryptedPass");
        userEntity.setRole(RoleEnum.ROLE_CUSTOMER);
    }

    @Test
    @DisplayName("Debería guardar un usuario si no existe previamente")
    void saveUser_whenUserDoesNotExist_shouldSaveAndReturnUser() {
        // Arrange
        // 1. Simular que el usuario no existe en la BD
        when(userRepository.findByEmail(userDomain.getEmail())).thenReturn(Optional.empty());
        // 2. Simular el mapeo de dominio a entidad
        when(userEntityMapper.toUserEntity(userDomain)).thenReturn(userEntity);
        // 3. Simular la operación de guardado en el repositorio
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        // 4. Simular el mapeo de vuelta de entidad a dominio
        when(userEntityMapper.toUser(userEntity)).thenReturn(userDomain);

        // Act
        User savedUser = userJpaAdapter.saveUser(userDomain);

        // Assert
        assertNotNull(savedUser);
        assertEquals(userDomain.getEmail(), savedUser.getEmail());

        // Verificar que todos los mocks fueron llamados en el orden correcto
        verify(userRepository, times(1)).findByEmail(userDomain.getEmail());
        verify(userEntityMapper, times(1)).toUserEntity(userDomain);
        verify(userRepository, times(1)).save(userEntity);
        verify(userEntityMapper, times(1)).toUser(userEntity);
    }

    @Test
    @DisplayName("Debería lanzar UserAlreadyExistsException si el email ya existe")
    void saveUser_whenUserAlreadyExists_shouldThrowException() {
        // Arrange
        // 1. Simular que el usuario ya existe en la BD
        when(userRepository.findByEmail(userDomain.getEmail())).thenReturn(Optional.of(userEntity));

        // Act & Assert
        // 2. Verificar que se lanza la excepción correcta
        assertThrows(UserAlreadyExistsException.class, () -> {
            userJpaAdapter.saveUser(userDomain);
        });

        // 3. Verificar que el flujo se detuvo y no se intentó mapear ni guardar
        verify(userEntityMapper, never()).toUserEntity(any(User.class));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Debería devolver un usuario cuando se encuentra por ID")
    void getUserById_whenUserFound_shouldReturnUser() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUser(userEntity)).thenReturn(userDomain);

        // Act
        User foundUser = userJpaAdapter.getUserById(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(userEntityMapper, times(1)).toUser(userEntity);
    }

    @Test
    @DisplayName("Debería devolver null si el usuario no se encuentra por ID")
    void getUserById_whenUserNotFound_shouldReturnNull() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // El mapper recibirá un null del orElse(null) y debería devolver null
        when(userEntityMapper.toUser(null)).thenReturn(null);

        // Act
        User foundUser = userJpaAdapter.getUserById(userId);

        // Assert
        assertNull(foundUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userEntityMapper, times(1)).toUser(null);
    }
}