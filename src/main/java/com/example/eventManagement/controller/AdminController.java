package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Registration;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.repository.RegistrationRepository;
import com.example.eventManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping("/admin")
    public String adminPage() {
        return "admin_page"; // existing dashboard
    }

    @GetMapping("/admin/users")
    public String manageUsersPage(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin_users"; // new Thymeleaf template
    }

    @GetMapping("/admin/reports")
    public String showReports(Model model) {
        // 1️⃣ Total users
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        // 2️⃣ Booking overview
        List<Registration> registrations = registrationRepository.findAll();
        long pending = registrations.stream().filter(r -> "Pending".equalsIgnoreCase(r.getStatus())).count();
        long approved = registrations.stream().filter(r -> "Approved".equalsIgnoreCase(r.getStatus())).count();
        long cancelled = registrations.stream().filter(r -> "Cancelled".equalsIgnoreCase(r.getStatus())).count();

        model.addAttribute("pendingBookings", pending);
        model.addAttribute("approvedBookings", approved);
        model.addAttribute("cancelledBookings", cancelled);

        // 3️⃣ Total revenue
        double totalRevenue = registrations.stream().mapToDouble(r -> r.getTotalPrice() != null ? r.getTotalPrice() : 0).sum();
        model.addAttribute("totalRevenue", totalRevenue);

        // 4️⃣ Most booked events
        List<Object[]> mostBookedEvents = registrationRepository.findEventsByMostBookings();
        model.addAttribute("mostBookedEvents", mostBookedEvents);

        return "admin_reports";
    }
}
