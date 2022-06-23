// (C) 2022 PPI AG
package de.sist.csc;

import de.sist.csc.model.Event;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;


@RestController
public class CalendarWeb {


    @GetMapping(value = "/api/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getTicketInfo() throws Exception {
        final LocalDateTime start = LocalDateTime.of(2022, Month.JUNE, 23, 10, 0);
        final LocalDateTime end = LocalDateTime.of(2022, Month.JUNE, 23, 15, 0);
        return Arrays.asList(new Event(start, end, "Simon", "title", "text"));
    }

}
