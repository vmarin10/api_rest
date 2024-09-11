package com.mercadona.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.mercadona.api.constants.ApiConstants.LOGIN_ENDPOINT;
import static com.mercadona.api.constants.ApiConstants.REGISTER_ENDPOINT;

/**
 * SecurityConfig is the class that configures the security of the application using Spring Security.
 * It defines the filters and authentication mechanisms, including JWT-based security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor to inject required dependencies for the security configuration.
     *
     * @param userDetailsService the service to load user details from the database
     * @param jwtFilter          the custom filter for JWT token validation
     * @param passwordEncoder    the encoder used to hash passwords
     */
    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Configures the security filter chain, defining which endpoints require authentication
     * and specifying the JWT filter for token validation.
     *
     * @param http the HttpSecurity object for configuring security settings
     * @return the configured SecurityFilterChain
     * @throws Exception if any error occurs during the configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(LOGIN_ENDPOINT, REGISTER_ENDPOINT).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configures the authentication provider that handles user authentication, using
     * a DAO-based approach with password encoding.
     *
     * @return the configured AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    /**
     * Provides an instance of AuthenticationManager, responsible for processing authentication requests.
     *
     * @param config the AuthenticationConfiguration object
     * @return the AuthenticationManager
     * @throws Exception if an error occurs during manager creation
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
}
