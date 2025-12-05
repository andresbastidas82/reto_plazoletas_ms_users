package com.pragma.ms_users.domain.api;

public interface IAuthenticationServicePort {

    String authenticate(String user, String password);
}
