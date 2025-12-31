package com.ebank.authService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
@Data
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String email;
}
