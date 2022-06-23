// (C) 2022 PPI AG
package de.sist.csc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private LocalDateTime start;
    private LocalDateTime end;
    private String user;
    private String title;
    private String text;

}
