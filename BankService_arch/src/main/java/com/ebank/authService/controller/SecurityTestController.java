package com.ebank.authService.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class SecurityTestController {

    @PreAuthorize("hasRole('AGENT_GUICHET')")
    @GetMapping("/agent")
    public String agentOnly() {
        return "AGENT OK";
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/client")
    public String clientOnly() {
        return "CLIENT OK";
    }
}
