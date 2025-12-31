package com.ebank.account_microservice.dto;


import com.ebank.account_microservice.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long accountId,
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDateTime transactionDate
) {}