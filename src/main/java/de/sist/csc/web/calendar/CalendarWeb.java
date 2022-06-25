package de.sist.csc.web.calendar;

import de.sist.csc.calendar.Calendar;
import de.sist.csc.calendar.CalendarCalculations;
import de.sist.csc.calendar.Registration;
import de.sist.csc.web.base.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class CalendarWeb {

    @Autowired
    private Calendar calendar;


    @GetMapping(value = "/api/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Registration> getRegistrationsDays(@RequestParam(required = false) Integer daysOffsetStartAfter, @RequestParam(required = false) Integer daysOffsetEndBefore) throws Exception {
        return calendar.getRegistrationsStartEndBetween(daysOffsetStartAfter, daysOffsetEndBefore);
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
    public ConflictCheckResponse checkForConflicting(@RequestBody Registration registration, @RequestParam Integer repetitions) throws Exception {
        if (repetitions == null || repetitions == 1) {
            List<Registration> conflicts = findConflicts(registration);
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

        final List<Registration> allBlockingConflicts = new ArrayList<>();
        final List<Registration> allShiftCausingConflicts = new ArrayList<>();
        final List<Registration> unchangedRegistrations = new ArrayList<>();
        final List<Registration> shiftedRegistrations = new ArrayList<>();
        final List<Registration> blockedRegistrations = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            Registration copy = registration.copy();
            copy.shiftWeeks(i);
            final List<Registration> conflicts = findConflicts(copy);
            if (conflicts.isEmpty()) {
                unchangedRegistrations.add(copy);
                continue;
            }
            final Registration shifted = tryShift(copy);
            if (!shifted.equals(copy)) {
                shiftedRegistrations.add(shifted);
                allShiftCausingConflicts.addAll(conflicts);
            } else {
                blockedRegistrations.add(copy);
                allBlockingConflicts.addAll(conflicts);
            }
        }
        return new ConflictCheckResponse(allBlockingConflicts, allShiftCausingConflicts, unchangedRegistrations, shiftedRegistrations, blockedRegistrations);

    }

    //Wir führen das mehrfach aus, um alle Konflikte zu findend und zu behandeln
    private Registration tryShift(Registration registration) {
        Registration shifted = registration.copy();
        List<Registration> conflicts = findConflicts(shifted);
        if (conflicts.isEmpty()) {
            return shifted;
        }

        shifted = CalendarCalculations.tryShiftRegistration(shifted, conflicts);
        conflicts = findConflicts(shifted);
        if (conflicts.isEmpty()) {
            return shifted;
        }

        shifted = CalendarCalculations.tryShiftRegistration(shifted, conflicts);
        conflicts = findConflicts(shifted);
        if (conflicts.isEmpty()) {
            return shifted;
        }
        return registration;
    }

    private List<Registration> findConflicts(Registration registration) {
        return calendar.getRegistrations().stream().filter(x -> CalendarCalculations.isOverlapping(x, registration)).collect(Collectors.toList());
    }

    @Data
    private static class ShiftResponse {
        private final List<Registration> remainingConflicts;
        private final boolean shifted;

    }


}
