// (C) 2022 PPI AG
package de.sist.csc.calendar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    public synchronized List<Registration> getRegistrationsStartEndBetween(Integer daysOffsetStartAfter, Integer daysOffsetEndBefore) {
        final Instant startAfter = Instant.now().minus(daysOffsetStartAfter == null ? 100 : daysOffsetStartAfter, ChronoUnit.DAYS);
        final Instant endBefore = Instant.now().plus(daysOffsetEndBefore == null ? 100 : daysOffsetEndBefore, ChronoUnit.DAYS);
        return repo.findAllByStartAfterAndEndBefore(startAfter, endBefore);
    }

    public synchronized void addRegistration(Registration registration, int repetitions) {
        log.info("Adding registration {} with {} repetitions", repetitions, registration);
        for (int i = 0; i < repetitions; i++) {
            registration.shiftWeeks(i);
            if (i > 0) {
                log.info("Adding repeated reservation with start {} and end {}", registration.getStart(), registration.getEnd());
            }
            repo.save(registration);
        }
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
