package com.pragma.ms_users.domain.spi;

public interface IPasswordEncoderServicePort {

    String passwordEncrypt(String password);
}
