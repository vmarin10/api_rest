package com.mercadona.api.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiConstants {

    public static final int STRENGTH_PASS_ENCODER = 12;
    public static final String LOGIN_ENDPOINT = "login";
    public static final String REGISTER_ENDPOINT = "register";
    public static final String ERROR_USER_AUTHENTICATED = "Error, this user does not exists";
    public static final String ERROR_CREATE_SECRET_KEY = "Error generando la clave secreta para JWT";

    public static final String KEY_GENERATOR_ALGORITHM = "HmacSHA256";
    public static final String BEARER_TOKEN_AUTH_TYPE = "Bearer ";
}
