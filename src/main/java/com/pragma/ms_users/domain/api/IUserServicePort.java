package com.pragma.ms_users.domain.api;

import com.pragma.ms_users.domain.model.User;

public interface IUserServicePort {

    User saveUser(User user);

}
