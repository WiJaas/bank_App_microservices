package com.ebank.authService.controller;


import com.ebank.authService.dto.CreateUserRequest;
import com.ebank.authService.dto.CreateUserResponse;
import com.ebank.authService.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public CreateUserResponse createClient(@RequestBody CreateUserRequest request) {
        return authenticationService.registerClient(request);
    }
}

