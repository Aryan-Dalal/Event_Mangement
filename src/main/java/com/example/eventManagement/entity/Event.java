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

    @Column(name="time")
    private LocalTime time;

    @Column(name="location")
    private String location;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="category")
    private String category;

    @Column(name="address")
    private String address;

    @Column(name = "veg_price")
    private Double vegPrice;

    @Column(name = "nonveg_price")
    private Double nonvegPrice;

    @Column(name = "decoration_price")
    private Double decorationPrice;

    @Column(name = "capacity")
    private String capacity;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Registration> registrations;
}
