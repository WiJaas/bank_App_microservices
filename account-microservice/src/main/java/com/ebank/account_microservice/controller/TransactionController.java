package com.ebank.account_microservice.controller;


import com.ebank.account_microservice.dto.CreateTransactionRequest;
import com.ebank.account_microservice.dto.TransactionResponse;
import com.ebank.account_microservice.dto.TransferRequest;
import com.ebank.account_microservice.service.AccountService;
import com.ebank.account_microservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public void transfer(@RequestBody TransferRequest request) {
        transactionService.transfer(request);
    }

    @GetMapping("/account/{accountId}")
    public Page<TransactionResponse> getTransactionsForAccount(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page
    ) {
        return transactionService.getTransactionsForAccount(accountId, page);
    }

}
