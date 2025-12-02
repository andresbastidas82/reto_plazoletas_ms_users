package com.pragma.ms_users.domain.model;


import com.pragma.ms_users.domain.model.enums.DocumentTypeEnum;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    private Long id;
    private String name;
    private String lastName;
    private DocumentTypeEnum documentType;
    private String documentNumber;
    private String cellphone;
    private String birthDate;
    private String email;
    private String password;
    private RoleEnum role;

}
