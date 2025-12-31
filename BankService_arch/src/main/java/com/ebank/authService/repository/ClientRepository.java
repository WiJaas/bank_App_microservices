package com.ebank.authService.repository;

import com.ebank.authService.model.Client;
import com.ebank.authService.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByCin(String cin);

    Optional<Client> findByEmail(String email);

    boolean existsByCin(String cin);

    boolean existsByEmail(String email);

    Page<Client> findByActive(boolean active, Pageable pageable);
    Optional<Client> findByUser(User user);



}