package com.ebank.account_microservice.repository;


import com.ebank.account_microservice.model.Account;
import com.ebank.account_microservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);
}
