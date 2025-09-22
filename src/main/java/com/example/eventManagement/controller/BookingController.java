package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Event;
import com.example.eventManagement.entity.Registration;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.repository.EventRepository;
import com.example.eventManagement.repository.RegistrationRepository;
import com.example.eventManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping
public class BookingController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    // ----------------- USER SIDE -----------------

    // Show event details for the currently logged-in user
    @GetMapping("/event/{eventId}")
    public String getEventDetail(@PathVariable("eventId") Integer eventId,
                                 Model model,
                                 Authentication authentication) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Event ID: " + eventId));

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Current user's booking
        Registration userBooking = getUserBooking(event, currentUser);

        // Check if ANY booking exists for this event
        Registration globalBooking = registrationRepository.findFirstByEvent(event).orElse(null);

        model.addAttribute("event", event);
        model.addAttribute("userBooking", userBooking);
        model.addAttribute("globalBooking", globalBooking); // 🔑 this tells if event is booked

        return "event-detail";
    }


    // Book event for currently logged-in user
    @PostMapping("/event/{eventId}/book")
    public String bookEvent(@PathVariable("eventId") int eventId,
                            Authentication authentication) {

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Event ID: " + eventId));

        // Prevent double booking
        if (registrationRepository.findByUserAndEvent(currentUser, event).isPresent()) {
            return "redirect:/bookings/already"; // page saying "You have already booked"
        }

        Registration registration = new Registration();
        registration.setUser(currentUser);
        registration.setEvent(event);
        registration.setStatus("Pending");
        registration.setRegistrationDate(LocalDateTime.now());

        registrationRepository.save(registration);

        return "redirect:/bookings/success";
    }

    // Booking success page
    @GetMapping("/bookings/success")
    public String bookingSuccess() {
        return "booking-success";
    }

    // Already booked page
    @GetMapping("/bookings/already")
    public String bookingAlready() {
        return "booking-already";
    }

    // ----------------- ADMIN SIDE -----------------

    // Admin: show all booking requests
    @GetMapping("/admin/requests")
    public String showAllBookings(Model model) {
        List<Registration> registrations = registrationRepository.findAllByOrderByRegistrationDateDesc();
        model.addAttribute("registrations", registrations);
        return "admin-bookings";
    }

    // Admin: approve or reject booking
    @PostMapping("/admin/requests/{registrationId}/status")
    public String updateBookingStatus(@PathVariable("registrationId") int registrationId,
                                      @RequestParam("status") String status) {

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Booking ID: " + registrationId));

        registration.setStatus(status);
        registrationRepository.save(registration);

        return "redirect:/admin/requests";
    }

    // Admin: delete rejected booking
    @PostMapping("/admin/requests/{registrationId}/delete")
    public String deleteRejectedBooking(@PathVariable("registrationId") int registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Booking ID: " + registrationId));

        if ("Rejected".equals(registration.getStatus())) {
            registrationRepository.delete(registration);
        }

        return "redirect:/admin/requests";
    }

    // Admin: view a specific user's booking for an event (safe)
    @GetMapping("/admin/event/{eventId}/user/{userId}")
    public String getEventDetailForUser(@PathVariable("eventId") int eventId,
                                        @PathVariable("userId") int userId,
                                        Model model) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Event ID: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID: " + userId));

        Registration userBooking = getUserBooking(event, user);

        model.addAttribute("event", event);
        model.addAttribute("userBooking", userBooking);
        model.addAttribute("viewedUser", user);

        return "admin-event-detail"; // create a Thymeleaf page for admin view
    }

    // ----------------- HELPER -----------------
    private Registration getUserBooking(Event event, User user) {
        return registrationRepository.findByUserAndEvent(user, event).orElse(null);
    }
}
