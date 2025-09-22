package com.example.eventManagement.repository;

import com.example.eventManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Fetch a user by their email (used as username in authentication)
    Optional<User> findByEmail(String email);

//    // If you want to fetch by name instead of email
//    Optional<User> findByUsername(String name);
}
