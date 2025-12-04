package com.pragma.ms_users.infrastructure.out.jpa.adapter;

import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.spi.IUserPersistencePort;
import com.pragma.ms_users.infrastructure.exception.UserAlreadyExistsException;
import com.pragma.ms_users.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.ms_users.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.pragma.ms_users.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User saveUser(User user) {
        UserEntity userValid = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userValid != null) {
            throw new UserAlreadyExistsException("The user already exists");
        }
        UserEntity userEntity = userEntityMapper.toUserEntity(user);
        return userEntityMapper.toUser(userRepository.save(userEntity));
    }

    @Override
    public User getUserById(Long userId) {
        return userEntityMapper.toUser(userRepository.findById(userId).orElse(null));
    }
}
