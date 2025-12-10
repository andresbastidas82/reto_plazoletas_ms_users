package com.pragma.ms_users.application.handler.impl;

import com.pragma.ms_users.application.dto.CustomerRequest;
import com.pragma.ms_users.application.dto.EmployeeRequest;
import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;
import com.pragma.ms_users.application.handler.IUserHandler;
import com.pragma.ms_users.application.mapper.UserRequestMapper;
import com.pragma.ms_users.domain.api.IUserServicePort;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final UserRequestMapper userRequestMapper;
    private final IUserServicePort userServicePort;


    @Override
    public UserResponse saveUserTypeOwner(UserRequest userRequest) {
        User user = userServicePort.saveUser(userRequestMapper.toUser(userRequest), RoleEnum.ROLE_OWNER);
        return userRequestMapper.toUserResponse(user);
    }

    @Override
    public UserResponse saveUserTypeEmployee(EmployeeRequest employeeRequest) {
        User user = userServicePort.saveUser(userRequestMapper.employeeToUser(employeeRequest), RoleEnum.ROLE_EMPLOYEE);
        return userRequestMapper.toUserResponse(user);
    }

    @Override
    public UserResponse saveUserTypeCustomer(CustomerRequest customerRequest) {
        User user = userServicePort.saveUser(userRequestMapper.customerToUser(customerRequest), RoleEnum.ROLE_CUSTOMER);
        return userRequestMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        return userRequestMapper.toUserResponse(userServicePort.getUserById(userId));
    }
}
