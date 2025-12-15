package com.pragma.ms_users.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequest {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The last name is required")
    private String lastName;

    @NotBlank(message = "The document type is required")
    private String documentType;

    @NotBlank(message = "The document number is required")
    @Pattern(regexp = "^\\d+$", message = "The identity document must contain only numbers")
    private String documentNumber;

    @NotBlank(message = "The cellphone is required")
    @Size(max = 13, message = "The phone number must have a maximum of 13 characters.")
    @Pattern(regexp = "^\\+?\\d{1,13}$", message = "The cell phone can only contain numbers and an optional '+'.")
    private String cellphone;

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not in a valid format.")
    private String email;

    @NotBlank(message = "The password is required")
    private String password;

    @NotNull(message = "The restaurant id is required")
    private Long restaurantId;

    private String role;
}
