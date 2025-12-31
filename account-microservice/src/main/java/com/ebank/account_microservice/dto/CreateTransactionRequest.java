package com.ebank.account_microservice.dto;


import com.ebank.account_microservice.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransactionRequest(
        @NotNull Long accountId,
        @NotNull TransactionType type,
        @Positive BigDecimal amount,
        String description
) {}
