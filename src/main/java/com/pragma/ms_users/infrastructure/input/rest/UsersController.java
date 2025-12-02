package com.pragma.ms_users.infrastructure.input.rest;

import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.handler.IUserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final IUserHandler userHandler;

    @PostMapping("/create-owner")
    public ResponseEntity<Void> createOwner(@Valid @RequestBody UserRequest userRequest) {
        userRequest.setRole("OWNER");
        userHandler.saveUserTypeOwner(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
