package com.example.eventManagement.service;
import com.example.eventManagement.entity.Registration;
import java.util.List;

public interface RegistrationService {
    public List<Registration> getAllRegistrartions();
    public Registration getRegistartionById(int id);
    public Registration addRegistration(Registration registration);
    public Registration updateRegistration(Registration registration);
    public void deleteRegistration(int id);
}
