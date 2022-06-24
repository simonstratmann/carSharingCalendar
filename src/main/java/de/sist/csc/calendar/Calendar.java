// (C) 2022 PPI AG
package de.sist.csc.calendar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class Calendar {

    @Autowired
    private RegistrationRepository repo;


    public synchronized List<Registration> getRegistrations() {
        return repo.findAll();
    }

    public synchronized void addRegistration(Registration registration) {
        log.info("Adding registration {}", registration);
        repo.save(registration);
    }

    public synchronized boolean deleteRegistration(long id) {
        final Optional<Registration> registration = repo.findById(id);
        if (registration.isPresent()) {
            log.info("Deleting registration {}", registration);
            repo.delete(registration.get());
            return true;
        } else {
            log.error("Unable to find registration with ID {}", id);
            return false;
        }

    }


}
