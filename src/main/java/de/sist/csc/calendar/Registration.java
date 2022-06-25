package de.sist.csc.calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


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

    @JsonIgnore
    public Registration copy() {
        return new Registration(this.getId(), this.getStart(), this.getEnd(), this.getUsername(), this.getTitle(), this.getText());
    }

    public void shiftWeeks(int weeks) {
        this.setStart(this.getStart().plus(7L * weeks, ChronoUnit.DAYS));
        this.setEnd(this.getEnd().plus(7L * weeks, ChronoUnit.DAYS));
    }

}
