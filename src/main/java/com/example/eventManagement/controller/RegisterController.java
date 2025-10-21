package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Authority;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.List;
import javax.sql.DataSource;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // register.html
    }

    @PostMapping("/register")
    @Transactional
    public String registerUser(@ModelAttribute("user") User user, Model model) {

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create default ROLE_USER authority
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setUser(user);

        // Link authority to user
        user.setAuthorities(List.of(authority));

        // Save user along with authority (cascade = ALL)
        userRepository.save(user);

        // Redirect to login
        return "redirect:/login?registered";
    }

}
