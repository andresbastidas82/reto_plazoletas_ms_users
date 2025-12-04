package com.pragma.ms_users.infrastructure.configuration;

import com.pragma.ms_users.domain.api.IUserServicePort;
import com.pragma.ms_users.domain.spi.IUserPersistencePort;
import com.pragma.ms_users.domain.usecase.UserUseCase;
import com.pragma.ms_users.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.pragma.ms_users.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.pragma.ms_users.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@RequiredArgsConstructor
public class BeanConfiguration {

    /*private final IUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }*/

    /*@Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort) {
        return new UserUseCase(userPersistencePort);
    }*/

}
