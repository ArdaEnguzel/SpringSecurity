package com.example.security.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenBuilder {
    private static final int expirationDuration = 5 * 60 * 1000;
    private static
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).claim("username",username).setIssuer("example").setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+ expirationDuration)).signWith(key).compact();
    }


    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt("Authorization").getBody();
    }

    public boolean isExpired(String token) {
        return  getClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));
    }

    public String getUsernameFromClaims(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if(getUsernameFromClaims(token)!= null && !isExpired(token)) {
            return true;
        }
        return false;
    }



}