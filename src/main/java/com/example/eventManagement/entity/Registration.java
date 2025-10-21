package com.example.eventManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private int registrationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status")
    private String status; // Pending, Confirmed, Rejected

    // -------------------- New Columns --------------------
    @Column(name = "veg_selected")
    private Boolean vegSelected = false;

    @Column(name = "nonveg_selected")
    private Boolean nonvegSelected = false;

    @Column(name = "veg_price")
    private Double vegPrice = 0.0;

    @Column(name = "nonveg_price")
    private Double nonvegPrice = 0.0;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    // -------------------- Payment Column --------------------
    @Column(name = "payment_status")
    private String paymentStatus = "Pending";  // Default: Pending
}
