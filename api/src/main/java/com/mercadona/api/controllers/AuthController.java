package com.mercadona.api.controllers;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mercadona.api.constants.ApiConstants.ERROR_USER_AUTHENTICATED;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para iniciar sesión de un usuario.
     *
     * @param user Datos de inicio de sesión.
     * @return Token JWT o mensaje de error si falla la autenticación.
     */
    @PostMapping("/login")
    public String login(@RequestBody UserModel user) {

        try {
            return authService.verify(user);
        } catch (AuthenticationException e) {
            return ERROR_USER_AUTHENTICATED;
        }
    }

}
