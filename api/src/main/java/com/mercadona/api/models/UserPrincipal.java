package com.mercadona.api.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import static com.mercadona.api.constants.ApiConstants.ROLE_USER;

/**
 * Class that implements the UserDetails interface to provide
 * user details to Spring Security.
 */
public record UserPrincipal(UserModel userModel) implements UserDetails {

    /**
     * Returns the authorities granted to the user.
     *
     * @return A collection of GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(ROLE_USER));
    }

    /**
     * Returns the user's password.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    /**
     * Returns the username.
     *
     * @return The username.
     */
    @Override
    public String getUsername() {
        return userModel.getName();
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the account is not expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return true if the account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials have expired.
     *
     * @return true if the credentials are not expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
