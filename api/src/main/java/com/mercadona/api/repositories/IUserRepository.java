package com.mercadona.api.repositories;

import com.mercadona.api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {

    UserModel findByName(String username);
}
