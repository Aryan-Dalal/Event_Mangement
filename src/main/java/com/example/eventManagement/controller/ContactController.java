package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Contact;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.repository.ContactRepository;
import com.example.eventManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Show Contact Page (prefill logged-in user info)
    @GetMapping
    public String showContactForm(Model model, Authentication authentication) {
        String email = authentication.getName(); // logged-in user's email

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Create a new Contact object
        Contact contact = new Contact();
        contact.setUser(user); // link logged-in user

        // Add to model
        model.addAttribute("contact", contact);
        model.addAttribute("user", user); // show name/email in the form

        return "contact"; // renders contact.html
    }

    // ✅ Handle Contact Submission
    @PostMapping("/submit")
    public String submitContact(@ModelAttribute("contact") Contact contact,
                                Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Set required fields from logged-in user
        contact.setUser(user);
        contact.setName(user.getName());   // important: NOT NULL
        contact.setEmail(user.getEmail()); // important: NOT NULL
        contact.setCreatedAt(LocalDateTime.now());

        // Save to database
        contactRepository.save(contact);

        // Redirect with success
        return "redirect:/contact?success";
    }
}
