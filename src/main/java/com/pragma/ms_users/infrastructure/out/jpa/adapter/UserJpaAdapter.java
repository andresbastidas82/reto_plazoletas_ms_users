package com.pragma.ms_users.infrastructure.out.jpa.adapter;

import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.spi.IUserPersistencePort;
import com.pragma.ms_users.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.ms_users.infrastructure.out.jpa.mapper.UserEntityMapper;
import com.pragma.ms_users.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = userEntityMapper.toUserEntity(user);
        return userEntityMapper.toUser(userRepository.save(userEntity));
    }
}
