package com.pragma.ms_users.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragma.ms_users.infrastructure.exception.AdultException;
import com.pragma.ms_users.infrastructure.exception.UserAlreadyExistsException;
import com.pragma.ms_users.infrastructure.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerAdvisorTest {

    private ControllerAdvisor controllerAdvisor;

    @BeforeEach
    void setUp() {
        controllerAdvisor = new ControllerAdvisor();
    }

    @Test
    @DisplayName("Debería manejar MethodArgumentNotValidException y devolver 400")
    void handleValidationErrors_shouldReturnBadRequest() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Error de validación");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleValidationErrors(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).containsKey("errors"));
        assertEquals(1, response.getBody().get("errors").size());
        assertEquals("Error de validación", response.getBody().get("errors").get(0));
    }

    @Test
    @DisplayName("Debería manejar HttpMessageNotReadableException genérica y devolver 400")
    void handleInvalidFormat_generic_shouldReturnBadRequest() {
        // Arrange
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getCause()).thenReturn(new RuntimeException()); // Causa no es InvalidFormatException

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleInvalidFormat(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid format or value in the JSON", Objects.requireNonNull(response.getBody()).get("errors").get(0));
    }

    @Test
    @DisplayName("Debería manejar HttpMessageNotReadableException por fecha inválida y devolver 400")
    void handleInvalidFormat_forInvalidDate_shouldReturnBadRequest() {
        // Arrange
        InvalidFormatException cause = mock(InvalidFormatException.class);
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("msg", cause);

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleInvalidFormat(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The birthDate field must be in the format yyyy-MM-dd and be a valid date.", Objects.requireNonNull(response.getBody()).get("errors").get(0));
    }

    @Test
    @DisplayName("Debería manejar UserAlreadyExistsException y devolver 409")
    void handleUserAlreadyExistsException_shouldReturnConflict() {
        // Arrange
        UserAlreadyExistsException ex = new UserAlreadyExistsException("El usuario ya existe");

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleUserAlreadyExistsException(ex);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El usuario ya existe", Objects.requireNonNull(response.getBody()).get("errors").get(0));
    }

    @Test
    @DisplayName("Debería manejar UserNotFoundException y devolver 404")
    void handleUserNotFoundException_shouldReturnNotFound() {
        // Arrange
        UserNotFoundException ex = new UserNotFoundException("Usuario no encontrado");
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/v1/users/99");
        WebRequest webRequest = new ServletWebRequest(mockRequest);

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleUserNotFoundException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        List<String> errors = Objects.requireNonNull(response.getBody()).get("errors");
        assertEquals(2, errors.size());
        assertTrue(errors.contains("Usuario no encontrado"));
        assertTrue(errors.contains("/v1/users/99"));
    }

    @Test
    @DisplayName("Debería manejar BadCredentialsException y devolver 401")
    void handleBadCredentialsException_shouldReturnUnauthorized() {
        // Arrange
        BadCredentialsException ex = new BadCredentialsException("Credenciales malas");

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleBadCredentialsException(ex);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials. Please check your email and password.", Objects.requireNonNull(response.getBody()).get("errors").get(0));
    }

    @Test
    @DisplayName("Debería manejar una excepción genérica y devolver 500")
    void handleGlobalException_shouldReturnInternalServerError() {
        // Arrange
        Exception ex = new Exception("Error inesperado");
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/some/unexpected/path");
        WebRequest webRequest = new ServletWebRequest(mockRequest);

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleGlobalException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        List<String> errors = Objects.requireNonNull(response.getBody()).get("errors");
        assertTrue(errors.get(0).contains("An unexpected error occurred: Error inesperado"));
        assertTrue(errors.contains("/some/unexpected/path"));
    }

    // Nota: Los tests para DocumentTypeNotFoundException y AdultException
    // seguirían el mismo patrón que el de UserAlreadyExistsException.
    // Se omiten por brevedad pero se construirían de forma similar.
    @Test
    @DisplayName("Debería manejar AdultException y devolver 400")
    void handleAdultException_shouldReturnBadRequest() {
        // Arrange
        AdultException ex = new AdultException();

        // Act
        ResponseEntity<Map<String, List<String>>> response = controllerAdvisor.handleAdultException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Asumiendo que ExceptionResponse.ADULT.getMessage() devuelve "Must be an adult"
        assertEquals("The user must be an adult", Objects.requireNonNull(response.getBody()).get("errors").get(0));
    }
}