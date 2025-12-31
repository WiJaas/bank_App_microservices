package com.ebank.customer_microservice.controller;

import com.ebank.customer_microservice.dao.Customer;
import com.ebank.customer_microservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService service;


    @PostMapping("/create")
    @PreAuthorize("hasRole('AGENT_GUICHET')")
    public Customer create(@RequestBody @Valid Customer customer) {
        return service.create(customer);
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) {
        return service.findById(id);
    }
}
