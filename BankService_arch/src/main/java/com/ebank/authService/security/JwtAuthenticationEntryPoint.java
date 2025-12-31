package com.ebank.authService.security;


import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Vous n’avez pas le droit d’accéder à cette fonctionnalité.\n" +
                        "Veuillez contacter votre administrateur");
    }
}
