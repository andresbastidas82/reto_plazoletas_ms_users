package com.pragma.ms_users.infrastructure.input.rest;

import com.pragma.ms_users.application.dto.CustomerRequest;
import com.pragma.ms_users.application.dto.EmployeeRequest;
import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;
import com.pragma.ms_users.application.handler.IUserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final IUserHandler userHandler;

    @PostMapping("/create-owner")
    public ResponseEntity<Void> createOwner(@Valid @RequestBody UserRequest userRequest) {
        userHandler.saveUserTypeOwner(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userHandler.getUserById(userId));
    }

    @PostMapping("/create-employee")
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        userHandler.saveUserTypeEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create-customer")
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody CustomerRequest customerRequest) {
        userHandler.saveUserTypeCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
