package com.example.eventManagement.service;
import com.example.eventManagement.entity.Event;
import com.example.eventManagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventServiceImp implements EventService{

    @Autowired
    private EventRepository eventRepository;
    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll() ;
    }

    @Override
    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(int id) {
         eventRepository.deleteById(id);
    }
}
