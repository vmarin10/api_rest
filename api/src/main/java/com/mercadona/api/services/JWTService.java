package com.mercadona.api.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.mercadona.api.constants.ApiConstants.ERROR_CREATE_SECRET_KEY;
import static com.mercadona.api.constants.ApiConstants.KEY_GENERATOR_ALGORITHM;

/**
 * Service class responsible for generating, validating, and extracting information from JWT tokens.
 * It uses HMAC SHA-256 algorithm for token signing.
 */
@Service
public class JWTService {

    private final String secretKey;

    public JWTService() {
        this.secretKey = generateSecretKey();
    }

    /**
     * Generates a JWT token for the given username.
     * The token contains claims and is signed using the secret key.
     *
     * @param username the username for which the token is generated
     * @return the generated JWT token
     */
    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the extracted username
     */
    public String extractUserName(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates if the JWT token is still valid based on username and expiration time.
     *
     * @param token       the JWT token to validate
     * @param userDetails the user details to compare with
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {

        final String userName = extractUserName(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Helper Methods

    /**
     * Extracts a specific claim from the JWT token using the provided claim resolver function.
     *
     * @param <T>           the type of the claim
     * @param token         the JWT token
     * @param claimResolver the function to extract the claim
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);

        return claimResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the extracted claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token the JWT token
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generates the secret key used for signing JWT tokens, using HmacSHA256 algorithm.
     *
     * @return the Base64-encoded secret key
     */
    private String generateSecretKey() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(KEY_GENERATOR_ALGORITHM);
            SecretKey sk = keyGen.generateKey();

            return Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(ERROR_CREATE_SECRET_KEY, e);
        }
    }

    /**
     * Decodes the Base64 secret key and returns a SecretKey object for token signing.
     *
     * @return the SecretKey object
     */
    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
