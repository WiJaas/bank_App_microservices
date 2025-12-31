package com.ebank.authService.controller;

import com.ebank.authService.dto.ClientResponse;
import com.ebank.authService.dto.CreateClientRequest;
import com.ebank.authService.service.ClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PreAuthorize("hasAuthority('AGENT_GUICHET')")
    @PostMapping("/create")
    public ClientResponse createClient(@Valid @RequestBody CreateClientRequest request) {
        return clientService.createClient(request);
    }


    @PreAuthorize("hasAuthority('AGENT_GUICHET')")
    @GetMapping("/{id}")
    public ClientResponse getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PreAuthorize("hasAuthority('AGENT_GUICHET') or hasAuthority('CLIENT')")
    @GetMapping("/by-cin/{cin}")
    public ClientResponse getClientByCin(@PathVariable String cin) {
        return clientService.getClientByCin(cin);
    }

    @PreAuthorize("hasAuthority('AGENT_GUICHET')")
    @GetMapping
    public Page<ClientResponse> listClients(
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ) {
        return clientService.listClients(active, pageable);
    }

//    @PreAuthorize("hasRole('AGENT_GUICHET')")
//    @PatchMapping("/{id}")
//    public ClientResponse updateClient(
//            @PathVariable Long id,
//            @Valid @RequestBody UpdateClientRequest request
//    ) {
//        return clientService.updateClient(id, request);
//    }

//    @PreAuthorize("hasRole('AGENT_GUICHET')")
//    @PatchMapping("/{id}/status")
//    public ClientResponse updateClientStatus(
//            @PathVariable Long id,
//            @RequestParam boolean active
//    ) {
//        return clientService.updateClientStatus(id, active);
//    }

    // For CLIENT dashboard: /clients/me
    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/me")
    public ClientResponse getMyProfile() {
        return clientService.getCurrentClientProfile();
    }
}
