package com.example.eventManagement.controller;

import com.example.eventManagement.entity.Event;
import com.example.eventManagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/")
    public String showDashboard(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "index"; // this will map to index.html
    }
}

