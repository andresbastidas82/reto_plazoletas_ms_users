package com.pragma.ms_users.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.ms_users.application.dto.CustomerRequest;
import com.pragma.ms_users.application.dto.EmployeeRequest;
import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;
import com.pragma.ms_users.application.handler.IUserHandler;
import com.pragma.ms_users.infrastructure.security.JwtService;
import com.pragma.ms_users.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserHandler userHandler;

    @MockBean
    private JwtService jwtService; // Necesario para satisfacer la dependencia de la configuración de seguridad

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con rol de ADMIN
    void createOwner_shouldReturnCreated() throws Exception {
        // Arrange
        UserRequest ownerRequest = UserRequest.builder()
                .name("John")
                .lastName("Doe")
                .documentType("CC")
                .documentNumber("123456789")
                .cellphone("+573001234567")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("owner@example.com")
                .password("password123")
                .build();

        // Act & Assert
        mockMvc.perform(post("/v1/users/create-owner")
                        .with(csrf()) // Añade el token CSRF para peticiones POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void getUserById_shouldReturnUserResponse() throws Exception {
        // Arrange
        Long userId = 1L;
        UserResponse userResponse = UserResponse.builder()
                .id(userId)
                .name("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .build();

        when(userHandler.getUserById(anyLong())).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
    }

    @Test
    @WithMockUser(roles = "OWNER") // Simula un usuario autenticado con rol de OWNER
    void createEmployee_shouldReturnCreated() throws Exception {
        // Arrange
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .name("Peter")
                .lastName("Jones")
                .documentType("CC")
                .documentNumber("987654321")
                .cellphone("+573007654321")
                .email("employee@example.com")
                .password("password123")
                .restaurantId(1L)
                .build();

        // Act & Assert
        mockMvc.perform(post("/v1/users/create-employee")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser // Un usuario anónimo o cualquiera puede registrarse
    void createCustomer_shouldReturnCreated() throws Exception {
        // Arrange
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name("Alice")
                .lastName("Smith")
                .documentType("CC")
                .documentNumber("555444333")
                .cellphone("+573005554444")
                .email("customer@example.com")
                .password("password123")
                .build();

        // Act & Assert
        mockMvc.perform(post("/v1/users/create-customer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated());
    }
}