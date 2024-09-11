package com.mercadona.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a User in the system.
 * Maps to the 'users' table in the database.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    /**
     * Name of the user. Cannot be null or empty.
     */
    @Column(nullable = false)
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    /**
     * Hashed password of the user.
     * It is recommended to use a secure password hashing function.
     */
    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /**
     * Email address of the user. Must be unique and follow the email format.
     */
    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

}
