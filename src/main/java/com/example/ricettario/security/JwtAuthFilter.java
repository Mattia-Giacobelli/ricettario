package com.example.ricettario.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Gira UNA VOLTA per ogni richiesta HTTP, prima che arrivi al controller.
 * Legge l'header Authorization, valida il token, e se è valido
 * "autentica" la richiesta agli occhi di Spring Security.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Nessun header, o non nel formato "Bearer <token>" -> lascia passare
        // senza autenticare; sarà SecurityConfig a decidere se quella rotta
        // richiede autenticazione o no.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // rimuove "Bearer "

        if (jwtUtil.isTokenValid(token)) {

            String username = jwtUtil.extractUsername(token);
            String permission = jwtUtil.extractPermission(token);

            // Spring Security si aspetta i ruoli con prefisso "ROLE_"
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + permission));

            var authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Da qui in poi, per questa richiesta, Spring Security sa chi sei
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}