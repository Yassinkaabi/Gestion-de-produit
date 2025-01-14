package com.poly.blogmanagment.Controllers;

import com.poly.blogmanagment.entities.user.UserModel;
//import com.poly.blogmanagment.repository.RoleRepository;
import com.poly.blogmanagment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public UserModel registerUser(@RequestBody UserModel user) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");


        return userRepository.save(user);
    }

    @PostMapping(value = "/api/admin/signup", consumes = "application/json")
    public UserModel registerAdmin(@RequestBody UserModel user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_ADMIN");


        return userRepository.save(user);
    }

}
