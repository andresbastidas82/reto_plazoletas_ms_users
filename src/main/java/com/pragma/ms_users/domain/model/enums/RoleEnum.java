package com.pragma.ms_users.domain.model.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ROLE_ADMIN("Administrador"),
    ROLE_OWNER("Propietario"),
    ROLE_EMPLOYEE("Empleado"),
    ROLE_CUSTOMER("Cliente");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }


}
