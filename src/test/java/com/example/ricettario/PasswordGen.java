package com.example.ricettario;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGen {

    @Test
    void generatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("");
        System.out.println("HASH_GENERATO: " + hash);
    }
}