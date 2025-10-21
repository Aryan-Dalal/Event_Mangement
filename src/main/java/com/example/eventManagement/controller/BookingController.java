package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Event;
import com.example.eventManagement.entity.Registration;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.repository.EventRepository;
import com.example.eventManagement.repository.RegistrationRepository;
import com.example.eventManagement.repository.UserRepository;
import com.example.eventManagement.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;

    // ----------------- USER SIDE -----------------
    @GetMapping("/event/{eventId}")
    public String getEventDetail(@PathVariable("eventId") Integer eventId,
                                 Model model,
                                 Authentication authentication,
                                 @ModelAttribute("errorMessage") String errorMessage,
                                 @ModelAttribute("successMessage") String successMessage) throws JsonProcessingException {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Event ID: " + eventId));

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Registration userBooking = getUserBooking(event, currentUser);

        List<Registration> bookedDates = registrationRepository.findByEvent(event);

        String bookedDatesJson = objectMapper.writeValueAsString(
                bookedDates.stream()
                        .map(r -> new DateRange(r.getStartDate(), r.getEndDate()))
                        .toList()
        );

        model.addAttribute("event", event);
        model.addAttribute("userBooking", userBooking);
        model.addAttribute("bookedDatesJson", bookedDatesJson);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("successMessage", successMessage);

        return "event-detail";
    }

    @PostMapping("/event/{eventId}/book")
    public String bookEvent(@PathVariable("eventId") int eventId,
                            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                            @RequestParam("vegSelected") boolean vegSelected,
                            @RequestParam("nonvegSelected") boolean nonvegSelected,
                            @RequestParam("vegPrice") double vegPrice,
                            @RequestParam("nonvegPrice") double nonvegPrice,
                            @RequestParam("totalPrice") double totalPrice,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Event ID: " + eventId));

        // Check if already booked
        if (registrationRepository.findByUserAndEvent(currentUser, event).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "You have already booked this event.");
            return "redirect:/event/" + eventId;
        }

        // Check overlapping dates
        List<Registration> existingBookings = registrationRepository.findByEvent(event);
        for (Registration r : existingBookings) {
            if (!(endDate.isBefore(r.getStartDate()) || startDate.isAfter(r.getEndDate()))) {
                redirectAttributes.addFlashAttribute("errorMessage", "The selected dates overlap with an existing booking.");
                return "redirect:/event/" + eventId;
            }
        }

        // Save registration with new fields
        Registration registration = new Registration();
        registration.setUser(currentUser);
        registration.setEvent(event);
        registration.setStatus("Pending"); // IMPORTANT: status remains Pending
        registration.setStartDate(startDate);
        registration.setEndDate(endDate);

        registration.setVegSelected(vegSelected);
        registration.setNonvegSelected(nonvegSelected);
        registration.setVegPrice(vegPrice);
        registration.setNonvegPrice(nonvegPrice);
        registration.setTotalPrice(totalPrice);

        registrationRepository.save(registration);

        // Redirect to payment page with registrationId
        return "redirect:/payment/" + registration.getRegistrationId();
    }

    // ----------------- PAYMENT PAGE -----------------
    @GetMapping("/payment/{registrationId}")
    public String showPaymentPage(@PathVariable("registrationId") int registrationId,
                                  Model model,
                                  @ModelAttribute("errorMessage") String errorMessage,
                                  @ModelAttribute("successMessage") String successMessage) {

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Registration ID: " + registrationId));

        model.addAttribute("registration", registration);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("successMessage", successMessage);

        return "payment";
    }


    // ----------------- PAYMENT CONFIRM -----------------
    @PostMapping("/payment/confirm/{registrationId}")
    public String confirmPayment(@PathVariable("registrationId") int registrationId,
                                 RedirectAttributes redirectAttributes) {

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Registration ID: " + registrationId));

        // Payment done, but status remains Pending
        registrationRepository.save(registration);

        // No email is sent
        redirectAttributes.addFlashAttribute("successMessage",
                "Payment successful! ✅ Booking is pending approval.");

        // Redirect back to event detail page showing Pending status
        return "redirect:/event/" + registration.getEvent().getEventId();
    }



    // ----------------- Booking Pages -----------------
    @GetMapping("/bookings/success")
    public String bookingSuccess() { return "booking-success"; }

    @GetMapping("/bookings/already")
    public String bookingAlready() { return "booking-already"; }

    @GetMapping("/bookings/overlap")
    public String bookingOverlap() { return "booking-overlap"; }

    // ----------------- ADMIN SIDE -----------------
    @GetMapping("/admin/requests")
    public String showAllBookings(Model model) {
        List<Registration> registrations = registrationRepository.findAllByOrderByStartDateDesc();
        model.addAttribute("registrations", registrations);
        return "admin-bookings";
    }

    // ----------------- UPDATE STATUS & SEND EMAIL -----------------
    @PostMapping("/admin/requests/{registrationId}/status")
    public String updateBookingStatus(@PathVariable("registrationId") int registrationId,
                                      @RequestParam("status") String status,
                                      RedirectAttributes redirectAttributes) {

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Booking ID: " + registrationId));

        status = status.trim();
        registration.setStatus(status);
        registrationRepository.save(registration);

        try {
            if ("Confirmed".equalsIgnoreCase(status)) {
                emailService.sendApprovalEmail(registration);
                redirectAttributes.addFlashAttribute("successMessage", "Booking approved and email sent successfully ✅");
            } else if ("Rejected".equalsIgnoreCase(status)) {
                emailService.sendRejectionEmail(registration);
                redirectAttributes.addFlashAttribute("successMessage", "Booking rejected and email sent successfully ❌");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Unknown status: " + status);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking status updated but email failed: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/admin/requests";
    }

    @PostMapping("/admin/requests/{registrationId}/delete")
    public String deleteRejectedBooking(@PathVariable("registrationId") int registrationId,
                                        RedirectAttributes redirectAttributes) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Booking ID: " + registrationId));
        if ("Rejected".equalsIgnoreCase(registration.getStatus())) {
            registrationRepository.delete(registration);
            redirectAttributes.addFlashAttribute("successMessage", "Rejected booking deleted successfully 🗑️");
        }
        return "redirect:/admin/requests";
    }

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

        return "admin-event-detail";
    }

    // ----------------- HELPER -----------------
    private Registration getUserBooking(Event event, User user) {
        return registrationRepository.findByUserAndEvent(user, event).orElse(null);
    }

    // ----------------- HELPER DTO -----------------
    private record DateRange(LocalDate from, LocalDate to) {}
}
