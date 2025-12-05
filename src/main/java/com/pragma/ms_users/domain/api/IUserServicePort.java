package com.pragma.ms_users.domain.api;

import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.RoleEnum;

public interface IUserServicePort {

    User saveUser(User user, RoleEnum role);

    User getUserById(Long userId);

}
