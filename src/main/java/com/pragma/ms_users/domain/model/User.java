package com.pragma.ms_users.domain.model;


import com.pragma.ms_users.domain.model.enums.DocumentTypeEnum;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate birthDate;
    private String email;
    private String password;
    private RoleEnum role;
    private Long restaurantId;

}
