package com.ebank.apiGateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Allow auth endpoints
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract claims from the token
        Claims claims = jwtUtil.extractAllClaims(token);

        // 1. Subject (email)
        String email = claims.getSubject();

        // 2. Roles (List)
        List<String> roles = claims.get("roles", List.class);

        // 3. Normalize role
        String role = null;
        if (roles != null && !roles.isEmpty()) {
            role = roles.get(0);
            if (role.startsWith("ROLE_")) {
                role = role.substring(5); // remove ROLE_
            }
        }

        // 4. Forward headers
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-Email", email)
                        .header("X-User-Role", role)
                        .build())
                .build();

        // Continue with the filtered request
        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

}