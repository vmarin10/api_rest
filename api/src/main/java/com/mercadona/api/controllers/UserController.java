package com.mercadona.api.controllers;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.mercadona.api.constants.ApiConstants.ERROR_USER_AUTHENTICATED;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param user Datos del usuario a registrar.
     * @return El usuario registrado.
     */
    @PostMapping("/register")
    public UserModel register(@RequestBody UserModel user) {
        return userService.register(user);
    }

    /**
     * Endpoint para iniciar sesión de un usuario.
     *
     * @param user Datos de inicio de sesión.
     * @return Token JWT o mensaje de error si falla la autenticación.
     */
    @PostMapping("/login")
    public String login(@RequestBody UserModel user) {
        try {
            return userService.verify(user);
        } catch (AuthenticationException e) {
            return ERROR_USER_AUTHENTICATED;
        }
    }

    /**
     * Endpoint para obtener la lista de todos los usuarios.
     *
     * @return Lista de usuarios.
     */
    @GetMapping
    public List<UserModel> getUsers() {
        return userService.getUsers();
    }

    /**
     * Endpoint para crear o actualizar un usuario.
     *
     * @param userModel Datos del usuario a crear/actualizar.
     * @return El usuario creado o actualizado.
     */
    @PostMapping
    public UserModel setUser(@RequestBody UserModel userModel) {
        return userService.setUser(userModel);
    }

    /**
     * Endpoint para obtener un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado o un Optional vacío.
     */
    @GetMapping("/{id}")
    public Optional<UserModel> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Endpoint para actualizar un usuario existente por su ID.
     *
     * @param userModelRequest Datos actualizados del usuario.
     * @param id               ID del usuario a actualizar.
     * @return Usuario actualizado.
     */
    @PutMapping("/{id}")
    public UserModel updateUserById(@RequestBody UserModel userModelRequest, @PathVariable Long id) {
        return userService.updateUserById(userModelRequest, id);
    }

    /**
     * Endpoint para eliminar un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Mensaje de éxito o error.
     */
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return "User " + id + " deleted successfully";
        }
        return "Error: User " + id + " could not be deleted";
    }

}
