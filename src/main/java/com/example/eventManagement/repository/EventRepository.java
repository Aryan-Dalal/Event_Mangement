package com.example.eventManagement.repository;
import com.example.eventManagement.entity.Event;
import com.example.eventManagement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
}
