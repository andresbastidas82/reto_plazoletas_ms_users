package com.pragma.ms_users.application.handler.impl;

import com.pragma.ms_users.application.dto.AuthenticationRequest;
import com.pragma.ms_users.application.dto.AuthenticationResponse;
import com.pragma.ms_users.application.handler.IAuthenticationHandler;
import com.pragma.ms_users.domain.api.IAuthenticationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationServicePort authenticationServicePort;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String token = authenticationServicePort.authenticate(request.getEmail(), request.getPassword());
        return AuthenticationResponse.builder().token(token).build();
    }
}
