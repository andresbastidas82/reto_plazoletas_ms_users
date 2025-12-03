package com.pragma.ms_users.application.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    private String role;
}
