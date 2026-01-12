package com.ebank.account_microservice.service;


import com.ebank.account_microservice.model.*;
import com.ebank.account_microservice.dto.*;
import com.ebank.account_microservice.repository.AccountRepository;
import com.ebank.account_microservice.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ClientProxy clientProxy;     // <-- Feign

    public AccountResponse createAccount(CreateAccountRequest request) {

        // 1) get client by CIN from AUTH-SERVICE
        ClientDto client = clientProxy.getClientByCin(request.cin());


        if (client == null || !client.isActive()) {
            throw new IllegalStateException("Client not found or inactive");
        }

        // 2) create account linked to remote client id
        Account account = Account.builder()
                .rib(generateRib())
                .clientId(client.getId())   // <-- we now use id from Auth MS
                .type(request.type())
                .status(AccountStatus.ACTIVE)
                .balance(request.initialBalance() == null
                        ? BigDecimal.ZERO
                        : request.initialBalance())
                .createdAt(LocalDateTime.now())
                .build();

        Account saved = accountRepository.save(account);

        return mapToAccountResponse(saved);
    }
    private Integer generateRib() {
        int min = 100_000_000;  // 9 digits
        int max = 999_999_999;

        int number;
        do {
            number = (int) (Math.random() * (max - min)) + min;
        } while (accountRepository.existsByRib(number));

        return number;
    }
    private AccountResponse mapToAccountResponse(Account a) {
        return new AccountResponse(
                a.getId(),
                a.getRib(),   // Integer
                a.getClientId(),
                a.getBalance(),
                a.getType(),
                a.getStatus()
        );

    }

    public List<AccountResponse> getAccountsByClient(Long clientId) {
        return accountRepository.findByClientId(clientId)
                .stream()
                .map(this::mapToAccountResponse)
                .toList();
    }




}