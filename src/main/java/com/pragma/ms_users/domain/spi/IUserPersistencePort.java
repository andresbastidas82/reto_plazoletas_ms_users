package com.pragma.ms_users.domain.spi;

import com.pragma.ms_users.domain.model.User;

public interface IUserPersistencePort {

    User saveUser(User user);

    User getUserById(Long userId);

}
