package com.ebank.authService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateClientRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String cin;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address;

    @NotNull
    @Past
    private LocalDate birthDate;
}


