package com.pragma.ms_users.application.handler;

import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;
import com.pragma.ms_users.application.handler.impl.UserHandler;
import com.pragma.ms_users.application.mapper.UserRequestMapper;
import com.pragma.ms_users.domain.api.IUserServicePort;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import com.pragma.ms_users.infrastructure.exception.AdultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UserHandlerTest {

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------
    // TEST 1 — Usuario adulto → debe guardar y retornar UserResponse
    // ------------------------------------------------------------
    @Test
    void saveUserTypeOwner_ShouldSaveUser_WhenUserIsAdult() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setBirthDate(LocalDate.now().minusYears(20));
        request.setPassword("123456");

        User mappedUser = new User();
        User savedUser = new User();
        UserResponse mappedResponse = new UserResponse();

        when(bCryptPasswordEncoder.encode("123456"))
                .thenReturn("ENCRYPTED_PASS");

        when(userRequestMapper.toUser(request))
                .thenReturn(mappedUser);

        when(userServicePort.saveUser(mappedUser, RoleEnum.ROLE_OWNER))
                .thenReturn(savedUser);

        when(userRequestMapper.toUserResponse(savedUser))
                .thenReturn(mappedResponse);

        // Act
        UserResponse response = userHandler.saveUserTypeOwner(request);

        // Assert
        assertNotNull(response);
        assertEquals(mappedResponse, response);

        verify(bCryptPasswordEncoder).encode("123456");
        verify(userRequestMapper).toUser(request);
        verify(userServicePort).saveUser(mappedUser, RoleEnum.ROLE_OWNER);
        verify(userRequestMapper).toUserResponse(savedUser);
    }


    // ------------------------------------------------------------
    // TEST 2 — Usuario menor de edad → debe lanzar AdultException
    // ------------------------------------------------------------
    @Test
    void saveUserTypeOwner_ShouldThrowException_WhenUserIsMinor() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setBirthDate(LocalDate.now().minusYears(17)); // < 18
        request.setPassword("123456");

        // Act & Assert
        assertThrows(AdultException.class, () -> {
            userHandler.saveUserTypeOwner(request);
        });

        verifyNoInteractions(bCryptPasswordEncoder);
        verifyNoInteractions(userRequestMapper);
        verifyNoInteractions(userServicePort);
    }


    // ------------------------------------------------------------
    // TEST 3 — Verificar que la contraseña sea encriptada
    // ------------------------------------------------------------
    @Test
    void saveUserTypeOwner_ShouldEncryptPassword() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setBirthDate(LocalDate.now().minusYears(25));
        request.setPassword("plainPass");

        User mappedUser = new User();
        User savedUser = new User();
        UserResponse mappedResponse = new UserResponse();

        when(bCryptPasswordEncoder.encode("plainPass"))
                .thenReturn("encrypted123");

        when(userRequestMapper.toUser(request))
                .thenReturn(mappedUser);

        when(userServicePort.saveUser(mappedUser, RoleEnum.ROLE_OWNER))
                .thenReturn(savedUser);

        when(userRequestMapper.toUserResponse(savedUser))
                .thenReturn(mappedResponse);

        // Act
        userHandler.saveUserTypeOwner(request);

        // Assert
        assertEquals("encrypted123", request.getPassword());
        verify(bCryptPasswordEncoder).encode("plainPass");
    }

}