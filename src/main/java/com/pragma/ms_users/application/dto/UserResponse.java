package com.pragma.ms_users.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponse {

    private Long id;
    private String name;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String cellphone;
    private String birthDate;
    private String email;
    private String password;
    private String role;

}
