package com.mercadona.api.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mercadona.api.constants.ApiConstants.KEY_GENERATOR_ALGORITHM;

@Service
public class JWTService {

    public JWTService() {}

    private String getSecretKey() {

        String strSecretKey;

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_GENERATOR_ALGORITHM);
            SecretKey secretKey = keyGenerator.generateKey();
            strSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return strSecretKey;
    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(getSecretKeySpec())
                .compact();
    }

    private Key getSecretKeySpec() {

        String secretKey = getSecretKey();

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
