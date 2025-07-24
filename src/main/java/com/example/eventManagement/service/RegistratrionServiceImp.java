package com.example.eventManagement.service;
import com.example.eventManagement.entity.Registration;
import com.example.eventManagement.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegistratrionServiceImp implements RegistrationService{

    @Autowired
    private RegistrationRepository registrationRepository;
    @Override
    public List<Registration> getAllRegistrartions() {
        return registrationRepository.findAll();
    }

    @Override
    public Registration getRegistartionById(int id) {
        return registrationRepository.findById(id).orElse(null);
    }

    @Override
    public Registration addRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    public Registration updateRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    public void deleteRegistration(int id) {
         registrationRepository.deleteById(id);
    }
}
