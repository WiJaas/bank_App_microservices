package com.ebank.authService.service;

import com.ebank.authService.dto.ClientResponse;
import com.ebank.authService.dto.CreateClientRequest;
import com.ebank.authService.exception.BusinessException;
import com.ebank.authService.exception.NotFoundException;
import com.ebank.authService.model.Client;
import com.ebank.authService.model.Role;
import com.ebank.authService.model.User;
import com.ebank.authService.repository.ClientRepository;
import com.ebank.authService.repository.RoleRepository;
import com.ebank.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Random RANDOM = new SecureRandom();


    @Override
    public ClientResponse createClient(CreateClientRequest request) {
        if (clientRepository.existsByCin(request.getCin())) {
            throw new BusinessException("CIN_ALREADY_EXISTS");
        }

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS");
        }

        String username = request.getCin();

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("USERNAME_ALREADY_EXISTS");
        }

        String rawPassword = generateRandomPassword(10);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new BusinessException("CLIENT_NOT_FOUND"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setRoles(Set.of(clientRole));

        userRepository.save(user);

        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setCin(request.getCin());
        client.setEmail(request.getEmail());
        client.setAddress(request.getAddress());
        client.setBirthDate(request.getBirthDate());
        client.setActive(true);
        client.setUser(user);

        clientRepository.save(client);

        System.out.println("Credentials sent to " + client.getEmail() +
                " -> username=" + username + " password=" + rawPassword);

        return toResponse(client);
    }

    @Override
    public ClientResponse getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CLIENT_NOT_FOUND"));
        return toResponse(client);

    }

    @Override
    public ClientResponse getClientByCin(String cin) {
        Client client = clientRepository.findByCin(cin)
                .orElseThrow(() -> new NotFoundException("CLIENT_NOT_FOUND"));
        return toResponse(client);
    }

    @Override
    public Page<ClientResponse> listClients(Boolean active, Pageable pageable) {
        if (active == null) {
            return clientRepository.findAll(pageable).map(this::toResponse);
        }
        return clientRepository.findByActive(active, pageable).map(this::toResponse);
    }


    public ClientResponse getCurrentClientProfile() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // JWT subject = email

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        var client = clientRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("CLIENT_NOT_FOUND"));

        return toResponse(client);
    }


    private ClientResponse toResponse(Client client) {
        ClientResponse dto = new ClientResponse();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setCin(client.getCin());
        dto.setEmail(client.getEmail());
        dto.setAddress(client.getAddress());
        dto.setBirthDate(client.getBirthDate());
        dto.setActive(client.isActive());
        dto.setUserId(client.getUser().getId());
        return dto;
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}