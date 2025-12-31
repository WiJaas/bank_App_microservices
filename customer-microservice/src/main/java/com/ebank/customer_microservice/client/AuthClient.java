package com.ebank.customer_microservice.client;


import com.ebank.customer_microservice.dto.CreateUserRequest;
import com.ebank.customer_microservice.dto.CreateUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "auth-service", path = "/internal/users")
public interface AuthClient {

    @PostMapping
    CreateUserResponse createUser(@RequestBody CreateUserRequest request);
}

