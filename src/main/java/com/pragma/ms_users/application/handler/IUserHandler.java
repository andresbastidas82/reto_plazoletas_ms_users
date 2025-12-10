package com.pragma.ms_users.application.handler;

import com.pragma.ms_users.application.dto.CustomerRequest;
import com.pragma.ms_users.application.dto.EmployeeRequest;
import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;

public interface IUserHandler {

    UserResponse saveUserTypeOwner(UserRequest userRequest);
    UserResponse saveUserTypeEmployee(EmployeeRequest employeeRequest);
    UserResponse saveUserTypeCustomer(CustomerRequest customerRequest);
    UserResponse getUserById(Long userId);

}
