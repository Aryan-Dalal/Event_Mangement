package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Event;
import com.example.eventManagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/wedding-venues")
    public String weddingVenues(Model model) {
        List<Event> weddingEvents = eventRepository.findByCategory("Wedding Venue");
        model.addAttribute("weddingEvents", weddingEvents);
        return "wedding_venues";
    }

    // Add similar mappings for:
    // /birthday-parties, /anniversaries, /family-gatherings, etc.
}

