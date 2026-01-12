package com.ebank.account_microservice.service;

import com.ebank.account_microservice.dto.CreateTransactionRequest;
import com.ebank.account_microservice.dto.TransactionResponse;
import com.ebank.account_microservice.dto.TransferRequest;
import com.ebank.account_microservice.model.Account;
import com.ebank.account_microservice.model.Transaction;
import com.ebank.account_microservice.model.TransactionType;
import com.ebank.account_microservice.repository.AccountRepository;
import com.ebank.account_microservice.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionResponse createTransaction(CreateTransactionRequest request) {

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

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

        transactionRepository.save(tx);

        return mapToTransactionResponse(tx);
    }

    @Transactional
    public void transfer(TransferRequest request) {

        createTransaction(new CreateTransactionRequest(
                request.sourceAccountId(),
                request.amount(),
                TransactionType.DEBIT,
                request.description()
        ));

        createTransaction(new CreateTransactionRequest(
                request.destinationAccountId(),
                request.amount(),
                TransactionType.CREDIT,
                request.description()
        ));
    }
    public Page<TransactionResponse> getTransactionsForAccount(
            Long accountId,
            int page
    ) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Pageable pageable = PageRequest.of(
                page,
                10, // 🔑 10 dernières opérations
                Sort.by(Sort.Direction.DESC, "transactionDate")
        );

        return transactionRepository.findByAccount(account, pageable)
                .map(this::mapToTransactionResponse);
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
