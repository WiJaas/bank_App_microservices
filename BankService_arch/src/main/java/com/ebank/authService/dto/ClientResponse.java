package com.ebank.authService.dto;


import lombok.Data;
import java.time.LocalDate;

@Data
public class ClientResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String cin;
    private String email;
    private String address;
    private LocalDate birthDate;
    private boolean active;
    private Long userId;
}
