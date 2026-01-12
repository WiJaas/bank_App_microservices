package com.ebank.account_microservice.dto;


import com.ebank.account_microservice.model.AccountStatus;
import com.ebank.account_microservice.model.AccountType;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        Integer rib,
        Long clientId,
        BigDecimal balance,
        AccountType type,
        AccountStatus status
) {}
