package com.pragma.ms_users.infrastructure.input.rest;

import com.pragma.ms_users.application.dto.AuthenticationRequest;
import com.pragma.ms_users.application.dto.AuthenticationResponse;
import com.pragma.ms_users.application.handler.IAuthenticationHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationHandler authenticationHandler;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationHandler.authenticate(request));
    }
}
