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
                .accountNumber(generateAccountNumber())
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
    private Integer generateAccountNumber() {
        int min = 100_000_000;  // 9 digits
        int max = 999_999_999;

        int number;
        do {
            number = (int) (Math.random() * (max - min)) + min;
        } while (accountRepository.existsByAccountNumber(number));

        return number;
    }
    private AccountResponse mapToAccountResponse(Account a) {
        return new AccountResponse(
                a.getId(),
                a.getAccountNumber(),   // Integer
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

    public TransactionResponse createTransaction(CreateTransactionRequest request) {

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        // handle debit / credit
        if (request.type() == TransactionType.DEBIT) {
            if (account.getBalance().compareTo(request.amount()) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            account.setBalance(account.getBalance().subtract(request.amount()));
        } else {
            account.setBalance(account.getBalance().add(request.amount()));
        }

        Transaction tx = Transaction.builder()
                .account(account)
                .type(request.type())
                .amount(request.amount())
                .description(request.description())
                .transactionDate(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(tx);

        return mapToTransactionResponse(saved);
    }
    public List<TransactionResponse> getTransactionsForAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        return transactionRepository.findByAccount(account)
                .stream()
                .map(this::mapToTransactionResponse)
                .toList();
    }
    private TransactionResponse mapToTransactionResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAccount().getId(),
                t.getType(),
                t.getAmount(),
                t.getDescription(),
                t.getTransactionDate()
        );
    }



}