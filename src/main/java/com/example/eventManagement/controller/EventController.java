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


    // --------------------------------------- Wedding Venues -------------------------------------------------------------

    @GetMapping("/wedding-venues")
    public String weddingVenues(Model model) {
        List<Event> weddingEvents = eventRepository.findByCategory("Wedding Venue");
        model.addAttribute("weddingEvents", weddingEvents);
        return "wedding_venues";
    }
//    passing model to the webpage with list of events


    @GetMapping("/wedding-venues/location/{location}")
    public String getWeddingVenuesByLocation(@PathVariable String location, Model model) {
        List<Event> events = eventRepository.findByCategoryIgnoreCaseAndLocationIgnoreCase("Wedding Venue", location);
        model.addAttribute("weddingEvents", events);
        return "wedding_venues";
    }
//    passing the location(String) and model list to the page


    // --------------------------------------- Anniversaries -------------------------------------------------------------

    @GetMapping("/anniversaries")
    public String anniversaryVenues(Model model) {
        List<Event> anniversaryEvents = eventRepository.findByCategoryIgnoreCase("Anniversaries");
        model.addAttribute("anniversaryEvents", anniversaryEvents);
        return "anniversaries"; // maps to anniversaries.html
    }
//    it is fetching the category of named Anniversaries From the DataBase and storing it in 'anniversaryEvents'and passing
//    it to anniversaries page


    @GetMapping("/anniversaries/location/{location}")
    public String anniversaryByLocation(@PathVariable String location, Model model) {
        List<Event> anniversaryEvents = eventRepository.findByCategoryIgnoreCaseAndLocationIgnoreCase("Anniversaries", location);
        model.addAttribute("anniversaryEvents", anniversaryEvents);
        return "anniversaries";
        // Passing the Location and The Category To the repo it is fetching all the Data and Storing it in anniversaryEvents
    }


    // --------------------------------------- Family Gatherings -------------------------------------------------------------

    @GetMapping("/family-gatherings")
    public String familyGatheringVenues(Model model) {
        List<Event> familyGatheringEvents = eventRepository.findByCategory("Family Gatherings");
        model.addAttribute("familyGatheringEvents", familyGatheringEvents);
        return "family_gatherings";
    }

    @GetMapping("/family-gatherings/location/{location}")
    public String familyGatheringByLocation(@PathVariable String location, Model model) {
        List<Event> familyGatheringEvents = eventRepository.findByCategoryIgnoreCaseAndLocationIgnoreCase("Family Gatherings", location);
        model.addAttribute("familyGatheringEvents", familyGatheringEvents);
        return "family_gatherings";
    }

    // --------------------------------------- BirthDay Parties  -------------------------------------------------------------

    @GetMapping("/birthday-parties")
    public String birthdayPartyVenues(Model model) {
        List<Event> birthdayPartyEvents = eventRepository.findByCategory("Birthday Parties");
        model.addAttribute("birthdayPartyEvents", birthdayPartyEvents);
        return "birthday_parties";
    }

    @GetMapping("/birthday-parties/location/{location}")
    public String birthdayPartyByLocation(@PathVariable String location, Model model) {
        List<Event> birthdayPartyEvents = eventRepository.findByCategoryIgnoreCaseAndLocationIgnoreCase("Birthday Parties", location);
        model.addAttribute("birthdayPartyEvents", birthdayPartyEvents);
        return "birthday_parties";

    }

}