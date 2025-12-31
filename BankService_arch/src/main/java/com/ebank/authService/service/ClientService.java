package com.ebank.authService.service;

import com.ebank.authService.dto.ClientResponse;
import com.ebank.authService.dto.CreateClientRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClientService {
    ClientResponse createClient(CreateClientRequest request);
    ClientResponse getClientById(Long id);
    ClientResponse getClientByCin(String cin);
    Page<ClientResponse> listClients(Boolean active, Pageable pageable);

   ClientResponse getCurrentClientProfile();
}
