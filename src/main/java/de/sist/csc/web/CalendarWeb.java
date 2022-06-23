// (C) 2022 PPI AG
package de.sist.csc.web;

import de.sist.csc.model.Registration;
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


@RestController
public class CalendarWeb {


    private final List<Registration> registrations = new ArrayList<>();

    public CalendarWeb() {
        final LocalDateTime start = LocalDateTime.of(2022, Month.JUNE, 23, 10, 0);
        final LocalDateTime end = LocalDateTime.of(2022, Month.JUNE, 23, 15, 0);
        registrations.addAll(Arrays.asList(new Registration(start, end, "Simon", "title", "text"),
                new Registration(start.minusDays(2), end.plusDays(1), "Simon", "title2", "text"),
                new Registration(start.minusDays(1), end.plusDays(2), "Simon", "title3", "text")
        ));
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

}
