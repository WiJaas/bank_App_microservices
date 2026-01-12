package com.ebank.account_microservice.dto;

import java.math.BigDecimal;

public record TransferRequest(
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        String description
) {}
