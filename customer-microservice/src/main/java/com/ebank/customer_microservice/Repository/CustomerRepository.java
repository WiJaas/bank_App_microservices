package com.ebank.customer_microservice.Repository;

import com.ebank.customer_microservice.dao.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCin(String cin);
    boolean existsByEmail(String email);
}

