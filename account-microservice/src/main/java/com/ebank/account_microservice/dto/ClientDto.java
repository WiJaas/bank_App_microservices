package com.ebank.account_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private Long id;

    private String cin;          // ← unique identifier

    private String firstName;
    private String lastName;
    private String email;

    // optional but consistent with your entity
    private String address;
    private LocalDate birthDate;

    // you may keep it if you need active/inactive flag
    private boolean active;
}
