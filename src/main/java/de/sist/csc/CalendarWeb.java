// (C) 2022 PPI AG
package de.sist.csc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PPI AG
 */
@RestController
public class CalendarWeb {


    @GetMapping(value = "/api/ticketInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTicketInfo(@RequestParam String keyword) throws Exception {
        if (keyword.contains("testError")) {
            throw new RuntimeException("Test");
        }
        return "hello";
    }

}
