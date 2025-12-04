package com.pragma.ms_users.domain.usecase;

import com.pragma.ms_users.domain.api.IUserServicePort;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    /*public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }*/

    @Override
    public User saveUser(User user) {
        return userPersistencePort.saveUser(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userPersistencePort.getUserById(userId);
    }
}
