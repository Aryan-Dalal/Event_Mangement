package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Event;
import com.example.eventManagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
//    passing model to the webpage with list of events



    @GetMapping("/events/location/{location}")
    public String getEventsByLocation(@PathVariable String location, Model model) {
        List<Event> events = eventRepository.findByLocationIgnoreCase(location);
        model.addAttribute("weddingEvents", events);
        return "wedding_venues"; // or the appropriate view name
    }
//    passing the location(String) and model list to the page

}

