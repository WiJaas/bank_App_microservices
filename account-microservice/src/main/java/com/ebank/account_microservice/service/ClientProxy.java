package com.ebank.account_microservice.service;

import com.ebank.account_microservice.dto.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AUTH-SERVICE")
public interface ClientProxy {

    @GetMapping("/clients/{id}")
    ClientDto getClientById(@PathVariable("id") Long id);

    @GetMapping("/clients/cin/{cin}")
    ClientDto getClientByCin(@PathVariable("cin") String cin);   // ← NEW
}
