package com.mercadona.api.controllers;

import com.mercadona.api.models.UserModel;
import com.mercadona.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserModel register(@RequestBody UserModel user) {
        return userService.register(user);

    }

    @PostMapping("/login")
    public String login(@RequestBody UserModel user) {

        return userService.verify(user);
    }

    @GetMapping
    public List<UserModel> getUsers() {
        return this.userService.getUsers();
    }

    @PostMapping
    public UserModel setUser(@RequestBody UserModel userModel) {
        return this.userService.setUser(userModel);
    }

    @GetMapping(path = "/{id}")
    public Optional<UserModel> getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
    }

    @PutMapping(path = "/{id}")
    public UserModel updateUserById(@RequestBody UserModel userModelRequest, Long id) {
        return this.userService.updateUserById(userModelRequest, id);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteUserById(@PathVariable Long id) {
        if(this.userService.deleteUser(id)) {
            return "User " + id + " deleted";
        }
        return "Error";
    }
}
