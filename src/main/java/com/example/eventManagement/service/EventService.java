package com.example.eventManagement.service;
import com.example.eventManagement.entity.Event;
import java.util.List;

public interface EventService {
    public List<Event> getAllEvents();
    public Event getEventById(int id);
    public Event updateEvent(Event event);
    public Event addEvent(Event event);
    public void deleteEvent(int id);
}
