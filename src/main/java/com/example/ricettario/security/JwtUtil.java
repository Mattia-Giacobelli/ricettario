package com.example.ricettario.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Legge la chiave segreta da application.properties (vedi in fondo alla
    // spiegazione)
    // NON metterla mai hardcoded nel codice sorgente.
    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretString.getBytes());
    }

    /**
     * Genera un token firmato contenente username e permission come claim.
     */
    public String generateToken(String username, String permission) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("permission", permission)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Estrae tutti i claim dal token. Lancia eccezione se il token
     * è scaduto, malformato, o la firma non è valida.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractPermission(String token) {
        return extractAllClaims(token).get("permission", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            // firma non valida, token malformato, scaduto, ecc.
            return false;
        }
    }
}