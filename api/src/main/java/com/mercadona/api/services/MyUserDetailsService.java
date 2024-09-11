package com.mercadona.api.services;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.models.UserPrincipal;
import com.mercadona.api.repositories.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service responsible for loading user-specific data.
 * Implements {@link UserDetailsService} to fetch user details from the database
 * and adapt them to Spring Security's {@link UserDetails} interface.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    /**
     * Constructor-based dependency injection for IUserRepository.
     * @param userRepository the user repository to interact with the database.
     */
    public MyUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user by username and maps it to a {@link UserDetails} implementation.
     * Throws {@link UsernameNotFoundException} if the user is not found.
     *
     * @param username the username identifying the user whose data is required.
     * @return {@link UserDetails} containing the user data.
     * @throws UsernameNotFoundException if no user is found with the provided username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new UserPrincipal(user);
    }
}
