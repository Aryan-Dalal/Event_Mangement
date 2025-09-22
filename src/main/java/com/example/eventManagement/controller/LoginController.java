package com.example.eventManagement.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html (Thymeleaf)

    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Destroy the session
        return "redirect:/login"; // Redirect back to login page
    }
}
