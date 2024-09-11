package com.mercadona.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.mercadona.api.constants.ApiConstants.STRENGTH_PASS_ENCODER;

@Configuration
public class EncryptConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder(STRENGTH_PASS_ENCODER);
    }
}
