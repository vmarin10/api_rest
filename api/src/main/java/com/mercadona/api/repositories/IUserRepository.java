package com.mercadona.api.repositories;

import com.mercadona.api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link UserModel} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {

    /**
     * Finds a user by their username.
     * @param username the username of the user.
     * @return the user entity that matches the given username.
     */
    UserModel findByName(String username);
}
