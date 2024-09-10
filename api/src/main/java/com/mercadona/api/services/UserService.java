package com.mercadona.api.services;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserModel register(UserModel user) {
        user.setPassword(encoder.encode(user.getPassword()));
        iUserRepository.save(user);
        return user;
    }

    public String verify(UserModel user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken();
        } else {
            return "fail";
        }
    }

    public List<UserModel> getUsers() {
        return iUserRepository.findAll();
    }

    public UserModel setUser(UserModel userModel) {
        return iUserRepository.save(userModel);
    }

    public Optional<UserModel> getUserById(Long id) {
        return iUserRepository.findById(id);
    }

    public UserModel updateUserById(UserModel userModelRequest, Long id) {
        UserModel userModel = iUserRepository.findById(id).get();

        userModel.setName(userModelRequest.getName());
        userModel.setEmail(userModelRequest.getEmail());

        return userModel;
    }

    public Boolean deleteUser(Long id) {
        try {
            iUserRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
