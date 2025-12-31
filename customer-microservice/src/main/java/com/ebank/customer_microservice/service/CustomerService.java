package com.ebank.customer_microservice.service;

import com.ebank.customer_microservice.Repository.CustomerRepository;
import com.ebank.customer_microservice.client.AuthClient;
import com.ebank.customer_microservice.dao.Customer;
import com.ebank.customer_microservice.dto.CreateUserRequest;
import com.ebank.customer_microservice.dto.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class CustomerService {

    private final CustomerRepository repository;
    private final AuthClient authClient;

    @Transactional
    public Customer create(Customer customer) {

        if (repository.existsByCin(customer.getCin()))
            throw new RuntimeException("CIN already exists");

        if (repository.existsByEmail(customer.getEmail()))
            throw new RuntimeException("Email already exists");

        // 1️⃣ Create USER in auth-service
        CreateUserResponse response =
                authClient.createUser(new CreateUserRequest(customer.getEmail()));


        // 2️⃣ Link customer ↔ user
        customer.setUserId(response.getUserId());

        return repository.save(customer);
    }
    public Customer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

}

