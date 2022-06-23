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

}
