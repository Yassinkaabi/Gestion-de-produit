package com.poly.blogmanagment.service;

import com.poly.blogmanagment.entities.user.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
    UserModel createUser(UserModel user);
    Optional<UserModel> findUserById(Long id);
    List<UserModel> findAllUsers();
    UserModel updateUser(Long id, UserModel updatedUser);
    void deleteUser(Long id);
}
