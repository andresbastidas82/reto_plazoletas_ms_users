package com.pragma.ms_users.infrastructure.out.jpa.entity;


import com.pragma.ms_users.domain.model.enums.DocumentTypeEnum;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocumentTypeEnum documentType;

    @Column(name = "document_number")
    private String documentNumber;

    private String cellphone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "restaurant_id")
    private Long restaurantId;
}
