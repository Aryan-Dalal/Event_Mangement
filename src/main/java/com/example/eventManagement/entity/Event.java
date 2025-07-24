package com.example.eventManagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="event_id")
    private int eventId;
    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="date")
    private LocalDate date;
    @Column(name="time")
    private LocalTime time;
    @Column(name="venue")
    private String venue;
    @Column(name="location")
    private String location;
    @Column(name="image_url")
    private String imageUrl;
    @Column(name="category")
    private String category;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Registration> registrations;
}
