package com.example.ricettario.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1. Gestione CORS per chiamate da React
                .cors(Customizer.withDefaults())

                // 2. Disabilita CSRF per tutte le API REST
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

                // 3. Autorizzazioni rotte
                .authorizeHttpRequests(auth -> auth
                        // Sblocca le richieste OPTIONS (CORS preflight) inviate da Axios/React
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/login", "/users/register", "/css/**", "/js/**", "/api/**", "/error")
                        .permitAll()
                        .requestMatchers("/home/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/ingredients/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/recipies", "/recipies/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/tags", "/tags/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/ratings", "/ratings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user", "/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())

                // 4. UNICO BLOCCO EXCEPTION HANDLING (Rimuovi qualsiasi altro
                // .exceptionHandling in fondo)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                        .accessDeniedPage("/access-denied"))

                // 5. Provider e Form Login per Thymeleaf
                .authenticationProvider(authenticationProvider)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED))

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(roleBasedSuccessHandler())
                        .failureUrl("/login?error"))

                // 6. Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login?logout"));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            var auths = authentication.getAuthorities();

            System.out.println("--- RUOLI TROVATI PER L'UTENTE AUTENTICATO ---");
            auths.forEach(a -> System.out.println("Ruolo letto: " + a.getAuthority()));
            System.out.println("----------------------------------------------");

            boolean isAdmin = auths.stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
            boolean isUser = auths.stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_USER"));

            if (isAdmin || isUser) {
                response.sendRedirect("/home");
            } else {
                response.sendRedirect("/login");
            }
        };
    }

}