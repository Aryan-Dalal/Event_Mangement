package com.example.eventManagement.repository;
import com.example.eventManagement.entity.Event;
import com.example.eventManagement.entity.Registration;
import com.example.eventManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Integer> {
    //List<Registration> findAllByOrderByRegistrationDateDesc();
    //Optional<Registration> findByEventAndUser(Event event, User user);


    Optional<Registration> findByUserAndEvent(User user, Event event);
    Optional<Registration> findFirstByEvent(Event event);
    List<Registration> findAllByOrderByStartDateDesc();
    List<Registration> findByEvent(Event event);

    // Count registrations grouped by event
    @Query("SELECT r.event AS event, COUNT(r) AS bookingsCount " +
            "FROM Registration r " +
            "GROUP BY r.event " +
            "ORDER BY COUNT(r) DESC")
    List<Object[]> findEventsByMostBookings();
}
