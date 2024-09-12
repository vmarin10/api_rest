package com.mercadona.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EndpointConfig {

    @Value("${endpoints.login}")
    private String loginEndpoint;

    @Value("${endpoints.register}")
    private String registerEndpoint;

}
