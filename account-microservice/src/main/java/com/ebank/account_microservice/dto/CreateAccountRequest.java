package com.ebank.account_microservice.dto;


import com.ebank.account_microservice.model.AccountType;

import java.math.BigDecimal;

public record CreateAccountRequest(
        String cin,
        BigDecimal initialBalance,
        AccountType type
) {}
