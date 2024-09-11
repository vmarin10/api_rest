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

@Service
public class JWTService {

    private final String secretKey;

    // Inicializa la clave secreta en el constructor
    public JWTService() {
        this.secretKey = generateSecretKey();
    }

    /**
     * Genera un token JWT basado en el nombre de usuario proporcionado.
     *
     * @param username El nombre de usuario para el cual se generará el token.
     * @return Token JWT.
     */
    public String generateToken(String username) {
        return createToken(new HashMap<>(), username);
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token El token JWT.
     * @return El nombre de usuario extraído del token.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Valida el token comparando el nombre de usuario y verificando que no haya expirado.
     *
     * @param token       El token JWT.
     * @param userDetails Información de usuario.
     * @return Verdadero si el token es válido, falso de lo contrario.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Verifica si el token ha expirado.
     *
     * @param token El token JWT.
     * @return Verdadero si el token ha expirado, falso de lo contrario.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Métodos privados de apoyo

    /**
     * Genera un token JWT basado en los claims y el nombre de usuario.
     *
     * @param claims   Información adicional para el payload del JWT.
     * @param username El nombre de usuario.
     * @return Token JWT.
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(getKey())
                .compact();
    }

    /**
     * Extrae un claim del token usando un resolvedor de claims.
     *
     * @param <T>            Tipo del claim.
     * @param token          El token JWT.
     * @param claimsResolver Función que define qué claim extraer.
     * @return El valor del claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token.
     *
     * @param token El token JWT.
     * @return Los claims del token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extrae la fecha de expiración del token.
     *
     * @param token El token JWT.
     * @return La fecha de expiración.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Genera una clave secreta usando HmacSHA256 y la codifica en Base64.
     *
     * @return La clave secreta en formato Base64.
     */
    private String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(KEY_GENERATOR_ALGORITHM);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(ERROR_CREATE_SECRET_KEY, e);
        }
    }

    /**
     * Obtiene la clave secreta para firmar y verificar el token.
     *
     * @return Clave secreta en formato SecretKey.
     */
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
