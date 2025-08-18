package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Event;
import com.example.eventManagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/venues")
public class VenueAdminController {

    @Autowired
    private EventRepository eventRepository;

    // Show all venues
    @GetMapping
    public String showVenues(Model model) {
        model.addAttribute("venues", eventRepository.findAll());
        model.addAttribute("event", new Event()); // empty form
        return "venues";
    }

    // Save (Add or Update)
    @PostMapping("/save")
    public String saveVenue(@ModelAttribute("event") Event event) {
        if (event.getImageUrl() != null && !event.getImageUrl().startsWith("/images/")) {
            event.setImageUrl("/images/" + event.getImageUrl());
        }
        eventRepository.save(event);
        return "redirect:/admin/venues";
    }

    // Edit venue (pre-fill form)
    @GetMapping("/edit/{id}")
    public String editVenue(@PathVariable("id") int id, Model model) {
        Event venue = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Venue ID: " + id));
        model.addAttribute("event", venue);
        model.addAttribute("venues", eventRepository.findAll());
        return "venues";
    }

    // Delete venue
    @PostMapping("/delete/{id}")
    public String deleteVenue(@PathVariable("id") int id) {
        eventRepository.deleteById(id);
        return "redirect:/admin/venues";
    }
}


