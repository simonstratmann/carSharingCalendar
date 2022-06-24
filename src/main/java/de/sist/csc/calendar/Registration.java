// (C) 2022 PPI AG
package de.sist.csc.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Registration {

    @Id
    @GeneratedValue
    private long id;
    private LocalDateTime start;
    @Column(name = "endtimestamp")
    private LocalDateTime end;
    private String username;
    private String title;
    private String text;

}
