package com.example.eventManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;   // auto-generated PK

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Foreign key to users table

    private String name;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now(); // default timestamp

}
