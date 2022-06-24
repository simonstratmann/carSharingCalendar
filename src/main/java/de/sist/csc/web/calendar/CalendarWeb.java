// (C) 2022 PPI AG
package de.sist.csc.web.calendar;

import de.sist.csc.calendar.Calendar;
import de.sist.csc.calendar.CalendarCalculations;
import de.sist.csc.calendar.Registration;
import de.sist.csc.web.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class CalendarWeb {

    @Autowired
    private Calendar calendar;


    @GetMapping(value = "/api/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Registration> getRegistrations() throws Exception {
        return calendar.getRegistrations();
    }


    @PostMapping(value = "/api/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response addRegistration(@RequestBody Registration registration) throws Exception {
        calendar.addRegistration(registration);
        return new Response(true, null);
    }

    @DeleteMapping(value = "/api/registrations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteRegistration(@PathVariable long id) throws Exception {
        calendar.deleteRegistration(id);
        return new Response(true, null);
    }

    @PostMapping(value = "/api/registrations/conflictCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConflictCheckResponse checkForConflicting(@RequestBody Registration registration) throws Exception {
        final List<Registration> conflicts = calendar.getRegistrations().stream().filter(x -> CalendarCalculations.isOverlapping(x, registration)).collect(Collectors.toList());
        log.debug("Found {} conflicts for {}: {}", conflicts.size(), registration, conflicts);
        if (conflicts.isEmpty()) {
            return ConflictCheckResponse.noConflicts(registration);
        }
        log.debug("Trying to shift registration to avoid conflicts");
        Registration shifted = tryShift(registration);
        if (!shifted.equals(registration)) {
            return ConflictCheckResponse.shifted(shifted, conflicts);
        }
        return ConflictCheckResponse.conflict(registration, conflicts);
    }

    //Wir führen das mehrfach aus, um alle Konflikte zu findend und zu behandeln
    private Registration tryShift(Registration registration) {
        Registration shifted = registration;
        List<Registration> conflicts = findConflicts(shifted);
        if (!conflicts.isEmpty()) {
            shifted = CalendarCalculations.tryShiftRegistration(registration, conflicts);
        }
        conflicts = findConflicts(shifted);
        if (!conflicts.isEmpty()) {
            shifted = CalendarCalculations.tryShiftRegistration(registration, conflicts);
        }
        conflicts = findConflicts(shifted);
        if (!conflicts.isEmpty()) {
            shifted = CalendarCalculations.tryShiftRegistration(registration, conflicts);
        }
        return shifted;
    }

    private List<Registration> findConflicts(Registration registration) {
        return calendar.getRegistrations().stream().filter(x -> CalendarCalculations.isOverlapping(x, registration)).collect(Collectors.toList());
    }


}
