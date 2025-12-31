package com.ebank.account_microservice.controller;


import com.ebank.account_microservice.dto.AccountResponse;
import com.ebank.account_microservice.dto.CreateAccountRequest;
import com.ebank.account_microservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    @GetMapping("/test")
    public String test() {
        return "account service OK";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/client/{clientId}")
    public List<AccountResponse> getAccountsByClient(@PathVariable Long clientId) {
        return accountService.getAccountsByClient(clientId);
    }
}
