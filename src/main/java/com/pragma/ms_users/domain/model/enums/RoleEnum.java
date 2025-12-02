package com.pragma.ms_users.domain.model.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ADMIN("Administrador"),
    OWNER("Propietario"),
    EMPLOYEE("Empleado"),
    CUSTOMER("Cliente");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }


}
