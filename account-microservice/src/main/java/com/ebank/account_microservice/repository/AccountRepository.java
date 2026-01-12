package com.ebank.account_microservice.repository;


import com.ebank.account_microservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByClientId(Long clientId);

    Optional<Account> findByRib(Integer accountNumber);
    boolean existsByRib(Integer rib);

}
