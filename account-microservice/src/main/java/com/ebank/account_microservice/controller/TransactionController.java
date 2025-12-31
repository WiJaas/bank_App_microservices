package com.ebank.account_microservice.controller;


import com.ebank.account_microservice.dto.CreateTransactionRequest;
import com.ebank.account_microservice.dto.TransactionResponse;
import com.ebank.account_microservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        return accountService.createTransaction(request);
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponse> getTransactionsForAccount(@PathVariable Long accountId) {
        return accountService.getTransactionsForAccount(accountId);
    }
}
