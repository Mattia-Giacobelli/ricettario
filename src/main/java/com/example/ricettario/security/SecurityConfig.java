package com.example.ricettario.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthFilter jwtAuthFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Esclude completamente i path degli upload dai filtri di sicurezza
        return (web) -> web.ignoring().requestMatchers("/uploads/**");
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // <-- questa catena vale solo per /api/**

                .csrf(csrf -> csrf.disable()) // niente sessione -> niente bisogno di CSRF token
                .cors(Customizer.withDefaults())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/recipes/**", "/api/tags", "/api/ai/**", "/uploads/recipes/**")
                        .permitAll()
                        .requestMatchers("/api/polls/active", "/api/polls/last-poll").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> response
                                .setStatus(HttpStatus.UNAUTHORIZED.value())));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults()) // qui la sessione c'è, quindi CSRF va tenuto attivo

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/users/register", "/css/**", "/js/**", "/error", "/api/tags",
                                "/api/ai/**", "/uploads/recipes/**")
                        .permitAll()
                        .requestMatchers("/home/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/ingredients/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/recipies", "/recipies/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/tags", "/tags/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/ratings", "/ratings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user", "/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())

                .authenticationProvider(authenticationProvider)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login"))
                        .accessDeniedPage("/access-denied"))

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(roleBasedSuccessHandler())
                        .failureUrl("/login?error"))

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