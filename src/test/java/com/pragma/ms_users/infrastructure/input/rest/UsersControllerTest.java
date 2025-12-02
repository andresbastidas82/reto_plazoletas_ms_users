package com.pragma.ms_users.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.handler.IUserHandler;
import com.pragma.ms_users.infrastructure.configuration.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = UsersController.class)
@Import(TestSecurityConfig.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserHandler userHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    void createOwner_whenValidRequest_shouldReturnCreatedAndCallHandler() throws Exception {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setName("John");
        userRequest.setLastName("Doe");
        userRequest.setCellphone("+573001234567");
        userRequest.setDocumentType("CEDULA_DE_CIUDADANIA");
        userRequest.setDocumentNumber("123456789");
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("ValidPassword123");
        userRequest.setBirthDate(LocalDate.of(1990, 1, 1));

        // Act
        mockMvc.perform(post("/users/create-owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        // --- AÑADE ESTA LÍNEA PARA INCLUIR EL TOKEN CSRF ---
                        .with(csrf()))
                .andExpect(status().isCreated()); // Assert (HTTP Status)

        // Assert (Handler Interaction)
        ArgumentCaptor<UserRequest> userRequestCaptor = ArgumentCaptor.forClass(UserRequest.class);
        verify(userHandler).saveUserTypeOwner(userRequestCaptor.capture());

        UserRequest capturedRequest = userRequestCaptor.getValue();
        assertEquals("OWNER", capturedRequest.getRole());
        assertEquals(userRequest.getEmail(), capturedRequest.getEmail());
        assertEquals(userRequest.getDocumentNumber(), capturedRequest.getDocumentNumber());
    }

    @Test
    @WithAnonymousUser
    void createOwner_whenInvalidRequest_shouldReturnBadRequest() throws Exception {
        // Arrange
        UserRequest invalidRequest = new UserRequest();

        // Act & Assert
        mockMvc.perform(post("/users/create-owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        // --- AÑADE ESTA LÍNEA TAMBIÉN AQUÍ ---
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        // Assert (Handler not called)
        verify(userHandler, never()).saveUserTypeOwner(any(UserRequest.class));
    }
}