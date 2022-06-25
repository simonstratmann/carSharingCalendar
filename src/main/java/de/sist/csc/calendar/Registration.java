// (C) 2022 PPI AG
package de.sist.csc.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Registration {

    @Id
    @GeneratedValue
    private long id;
    private Instant start;
    @Column(name = "endtimestamp")
    private Instant end;
    private String username;
    private String title;
    private String text;

}
