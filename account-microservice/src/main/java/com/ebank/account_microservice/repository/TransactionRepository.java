package com.ebank.account_microservice.repository;


import com.ebank.account_microservice.model.Account;
import com.ebank.account_microservice.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccount(
            Account account,
            Pageable pageable
    );
}
