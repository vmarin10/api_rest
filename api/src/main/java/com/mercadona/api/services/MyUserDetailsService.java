package com.mercadona.api.services;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.models.UserPrincipal;
import com.mercadona.api.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository iUserRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = iUserRepository.findByName(username);
        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(user);
    }
}
