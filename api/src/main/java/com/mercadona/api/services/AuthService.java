package com.mercadona.api.services;

import com.mercadona.api.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Class for login management.
 */
@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JWTService jwtService;


    @Autowired
    public AuthService(AuthenticationManager authManager, JWTService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    /**
     * Verificación de usuario para iniciar sesión y generación de token JWT.
     *
     * @param userModel Datos del usuario (nombre y contraseña).
     * @return Token JWT si la autenticación es exitosa.
     * @throws AuthenticationException Excepción si la autenticación falla.
     */
    public String verify(UserModel userModel) throws AuthenticationException {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userModel.getName(), userModel.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userModel.getName());
        }

        return null;
    }

}
