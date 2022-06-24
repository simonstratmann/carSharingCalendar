// (C) 2022 PPI AG
package de.sist.csc.calendar;

import de.sist.csc.model.Registration;
import de.sist.csc.web.ConflictCheckResponse;
import de.sist.csc.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings("RedundantIfStatement")
@RestController
@Slf4j
public class CalendarWeb {


    private final List<Registration> registrations = new ArrayList<>();

    public CalendarWeb() {
        registrations.addAll(Arrays.asList(getRegistration(13, 10, 17, 15, "title", "Kuni"),
                getRegistration(20, 10, 20, 22, "title", "Hilde"),
                getRegistration(23, 10, 23, 15, null, "Marianne"),
                getRegistration(23, 16, 23, 18, null, "Simon")
        ));
    }

    private static Registration getRegistration(int fromDay, int fromHour, int toDay, int toHour, String title, String user) {
        return new Registration(getLocalDateTime(fromDay, fromHour), getLocalDateTime(toDay, toHour), user, title, "text");
    }

    private static LocalDateTime getLocalDateTime(int dayOfMonth, int hour) {
        return LocalDateTime.of(2022, Month.JUNE, dayOfMonth, hour, 0);
    }

    @GetMapping(value = "/api/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Registration> getRegistrations() throws Exception {

        return registrations;
    }

    @PostMapping(value = "/api/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response addRegistration(@RequestBody Registration registration) throws Exception {
        registrations.add(registration);
        return new Response(true, null);
    }

    @PostMapping(value = "/api/registrationsConflictCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConflictCheckResponse checkForConflicting(@RequestBody Registration registration) throws Exception {
        final List<Registration> conflicts = registrations.stream().filter(x -> CalendarCalculations.isOverlapping(x, registration)).collect(Collectors.toList());
        log.debug("Found {} conflicts for {}: {}", conflicts.size(), registration, conflicts);
        if (conflicts.isEmpty()) {
            return ConflictCheckResponse.noConflicts(registration);
        }
        log.debug("Trying to shift registration to avoid conflicts");
        final Registration shifted = CalendarCalculations.tryShiftRegistration(registration, conflicts);
        if (shifted.equals(registration)) {
            return ConflictCheckResponse.shifted(shifted, conflicts);
        }
        return ConflictCheckResponse.conflict(registration, conflicts);
    }



}
