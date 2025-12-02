package com.pragma.ms_users.application.utils;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ROLE_OWNER = "OWNER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    public static final String DOCUMENT_TYPE_CC = "CEDULA_DE_CIUDADANIA";
    public static final String DOCUMENT_TYPE_CE = "CEDULA_DE_EXTRANJERIA";
    public static final String DOCUMENT_TYPE_PASSPORT = "PASAPORTE";
    public static final String DOCUMENT_TYPE_NIT = "NIT";

    public static final Integer OVER_18_YEARS_OLD = 18;

}
