package com.pragma.ms_users.domain.spi;

public interface IAuthenticateServicePort {

    String authenticate(String email, String password);
}
