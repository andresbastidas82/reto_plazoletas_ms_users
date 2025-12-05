package com.pragma.ms_users.infrastructure.security.adapter;

import com.pragma.ms_users.domain.spi.IPasswordEncoderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements IPasswordEncoderServicePort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String passwordEncrypt(String password) {
        return passwordEncoder.encode(password);
    }
}
