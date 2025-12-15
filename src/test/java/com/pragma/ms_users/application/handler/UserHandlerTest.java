package com.pragma.ms_users.application.handler;

import com.pragma.ms_users.application.dto.CustomerRequest;
import com.pragma.ms_users.application.dto.EmployeeRequest;
import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;
import com.pragma.ms_users.application.handler.impl.UserHandler;
import com.pragma.ms_users.application.mapper.UserRequestMapper;
import com.pragma.ms_users.domain.api.IUserServicePort;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.DocumentTypeEnum;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private IUserServicePort userServicePort;

    @InjectMocks
    private UserHandler userHandler;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        // Objetos comunes que se pueden reutilizar en los tests
        user = User.builder().id(1L).name("John").lastName("Doe").documentNumber("123456789").documentType(DocumentTypeEnum.CITIZENSHIP_CARD)
                .cellphone("+573001234567").email("john.doe@example.com").password("password").build();
        userResponse = UserResponse.builder().id(1L).name("John").lastName("Doe").email("john.doe@example.com")
                .cellphone("+573001234567").build();
    }

    @Test
    void saveUserTypeOwner_shouldMapRequestAndCallService() {
        // Arrange
        UserRequest ownerRequest = new UserRequest(); // Llenar con datos si es necesario
        when(userRequestMapper.toUser(any(UserRequest.class))).thenReturn(user);
        when(userServicePort.saveUser(any(User.class), eq(RoleEnum.ROLE_OWNER))).thenReturn(user);
        when(userRequestMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act
        UserResponse result = userHandler.saveUserTypeOwner(ownerRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getName(), result.getName());

        verify(userRequestMapper, times(1)).toUser(ownerRequest);
        verify(userServicePort, times(1)).saveUser(user, RoleEnum.ROLE_OWNER);
        verify(userRequestMapper, times(1)).toUserResponse(user);
    }

    @Test
    void saveUserTypeEmployee_shouldMapRequestAndCallService() {
        // Arrange
        EmployeeRequest employeeRequest = new EmployeeRequest();
        when(userRequestMapper.employeeToUser(any(EmployeeRequest.class))).thenReturn(user);
        when(userServicePort.saveUser(any(User.class), eq(RoleEnum.ROLE_EMPLOYEE))).thenReturn(user);
        when(userRequestMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act
        UserResponse result = userHandler.saveUserTypeEmployee(employeeRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());

        verify(userRequestMapper, times(1)).employeeToUser(employeeRequest);
        verify(userServicePort, times(1)).saveUser(user, RoleEnum.ROLE_EMPLOYEE);
        verify(userRequestMapper, times(1)).toUserResponse(user);
    }

    @Test
    void saveUserTypeCustomer_shouldMapRequestAndCallService() {
        // Arrange
        CustomerRequest customerRequest = new CustomerRequest();
        when(userRequestMapper.customerToUser(any(CustomerRequest.class))).thenReturn(user);
        when(userServicePort.saveUser(any(User.class), eq(RoleEnum.ROLE_CUSTOMER))).thenReturn(user);
        when(userRequestMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act
        UserResponse result = userHandler.saveUserTypeCustomer(customerRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());

        verify(userRequestMapper, times(1)).customerToUser(customerRequest);
        verify(userServicePort, times(1)).saveUser(user, RoleEnum.ROLE_CUSTOMER);
        verify(userRequestMapper, times(1)).toUserResponse(user);
    }

    @Test
    void getUserById_shouldCallServiceAndMapResponse() {
        // Arrange
        Long userId = 1L;
        when(userServicePort.getUserById(userId)).thenReturn(user);
        when(userRequestMapper.toUserResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse result = userHandler.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getEmail(), result.getEmail());

        verify(userServicePort, times(1)).getUserById(userId);
        verify(userRequestMapper, times(1)).toUserResponse(user);
    }
}