package com.pragma.ms_users.domain.model.enums;

import lombok.Getter;

@Getter
public enum DocumentTypeEnum {

    CITIZENSHIP_CARD("Cédula de ciudadanía", "CC"),
    FOREIGNER_IDENTITY_CARD("Cédula de extranjería", "CE"),
    PASSPORT("Pasaporte", "PAS"),
    NIT("Número único tributario", "NIT");

    private String documentName;
    private String abbreviation;

    DocumentTypeEnum(String documentName, String abbreviation) {
        this.documentName = documentName;
        this.abbreviation = abbreviation;
    }

}
