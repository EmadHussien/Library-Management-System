package com.emadsolutions.LMS.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;


    private boolean isTokenExpired(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public Date getIssuedAtDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getIssuedAt();
    }
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }
    private Claims getClaimsFromToken(String token) {
        try{
            byte[] apiKeySecretBytes = jwtSecret.getBytes();
            Key signingKey = Keys.hmacShaKeyFor(apiKeySecretBytes);
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (SignatureException e) {
            throw new JwtException("Invalid JWT token");
        }
    }

    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        byte[] apiKeySecretBytes = jwtSecret.getBytes();
        Key signingKey = Keys.hmacShaKeyFor(apiKeySecretBytes);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(signingKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
