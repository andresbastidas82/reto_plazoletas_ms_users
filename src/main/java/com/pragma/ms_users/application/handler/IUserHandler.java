package com.pragma.ms_users.application.handler;

import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;

public interface IUserHandler {

    UserResponse saveUserTypeOwner(UserRequest userRequest);

    UserResponse getUserById(Long userId);

}
