package com.mercadona.api.services;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    IUserRepository iUserRepository;

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
