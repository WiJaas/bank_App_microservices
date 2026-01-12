package com.ebank.account_microservice.dto;

import com.ebank.account_microservice.model.TransactionType;
import java.math.BigDecimal;


public record CreateTransactionRequest(
        Long accountId,
        BigDecimal amount,
        TransactionType type,
        String description
) {}

