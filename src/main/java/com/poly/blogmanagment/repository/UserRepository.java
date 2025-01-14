package com.poly.blogmanagment.repository;

import com.poly.blogmanagment.entities.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
