package com.pragma.ms_users.domain.usecase;

import com.pragma.ms_users.domain.api.IAuthenticationServicePort;
import com.pragma.ms_users.domain.spi.IAuthenticateServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCase implements IAuthenticationServicePort {

    private final IAuthenticateServicePort authenticateServicePort;

    @Override
    public String authenticate(String email, String password) {
        return authenticateServicePort.authenticate(email, password);
    }
}
