package com.mercadona.api.services;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository iUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository iUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Registro de un nuevo usuario con contraseña encriptada.
     *
     * @param user Datos del usuario.
     * @return Usuario registrado.
     */
    public UserModel register(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return iUserRepository.save(user);
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return Lista de usuarios.
     */
    public List<UserModel> getUsers() {
        return iUserRepository.findAll();
    }

    /**
     * Guarda o actualiza un usuario en la base de datos.
     *
     * @param userModel Datos del usuario.
     * @return Usuario guardado.
     */
    public UserModel setUser(UserModel userModel) {
        return iUserRepository.save(userModel);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario si se encuentra.
     */
    public Optional<UserModel> getUserById(Long id) {
        return iUserRepository.findById(id);
    }

    /**
     * Actualiza los datos de un usuario por su ID.
     *
     * @param userModelRequest Datos actualizados del usuario.
     * @param id               ID del usuario a actualizar.
     * @return Usuario actualizado.
     */
    public UserModel updateUserById(UserModel userModelRequest, Long id) {
        Optional<UserModel> optionalUser = iUserRepository.findById(id);

        if (optionalUser.isPresent()) {
            UserModel userModel = optionalUser.get();
            userModel.setName(userModelRequest.getName());
            userModel.setEmail(userModelRequest.getEmail());

            return iUserRepository.save(userModel);
        } else {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return True si se eliminó correctamente, False en caso contrario.
     */
    public boolean deleteUser(Long id) {
        if (iUserRepository.existsById(id)) {
            iUserRepository.deleteById(id);
            return true;
        }
        return false;
    }

/*
    // Métodos adicionales

    *//**
     * Verifica si un nombre de usuario está disponible.
     * @param username Nombre de usuario.
     * @return True si el nombre de usuario está disponible, False si ya existe.
     *//*
    public boolean isUsernameAvailable(String username) {
        return iUserRepository.findByName(username).isEmpty();
    }


    *//**
     * Busca usuarios por nombre de usuario.
     * @param username Nombre de usuario.
     * @return Lista de usuarios con el nombre especificado.
     *//*
    public List<UserModel> getUsersByUsername(String username) {
        return iUserRepository.findByUsernameContainingIgnoreCase(username);
    }

    *//**
     * Cambia la contraseña de un usuario.
     * @param id ID del usuario.
     * @param newPassword Nueva contraseña sin encriptar.
     * @return Usuario con la contraseña actualizada.
     *//*
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<UserModel> userOptional = iUserRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return false; // Usuario no encontrado
        }

        UserModel user = userOptional.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Verificar la contraseña antigua
        if (!encoder.matches(oldPassword, user.getPassword())) {
            return false; // Contraseña antigua incorrecta
        }

        // Actualizar la contraseña
        user.setPassword(encoder.encode(newPassword));
        iUserRepository.save(user);
        return true; // Contraseña cambiada exitosamente
    }*/


}
