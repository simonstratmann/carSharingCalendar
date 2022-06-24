// (C) 2022 PPI AG
package de.sist.csc.calendar;

import com.google.common.collect.Iterables;
import de.sist.csc.model.Registration;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("RedundantIfStatement")
public class CalendarCalculations {

    private CalendarCalculations() {
    }

    static boolean isOverlapping(Registration a, Registration b) {
        if (b.getStart().isBefore(a.getStart()) && b.getEnd().isAfter(a.getStart())) {
            return true;
        }
        if (b.getStart().isBefore(a.getEnd()) && b.getEnd().isAfter(a.getStart())) {
            return true;
        }
        return false;
    }

    static Registration tryShiftRegistration(Registration registration, List<Registration> conflicts) {
        final Registration shifted = new Registration(registration.getStart(), registration.getEnd(), registration.getUser(), registration.getTitle(), registration.getText());
        conflicts.sort(Comparator.comparing(Registration::getStart));
        final Registration earlier = conflicts.get(0);
        final Registration later = Iterables.getLast(conflicts);

        /*
        Nicht möglich:
        Konflikt:       |               |
        Registrierung:      |       |
        */

        if (isBefore(earlier.getStart(), shifted.getStart()) && isAfter(earlier.getEnd(), shifted.getEnd())) {
            return shifted;
        }
        if (isBefore(later.getStart(), shifted.getStart()) && isAfter(later.getEnd(), shifted.getEnd())) {
            return shifted;
        }

        /*
        Konflikt:       |       |
        Registrierung:      |       |
        Ergebnis:               |   |
        */
        if (isBefore(earlier.getStart(), shifted.getStart()) && isAfter(earlier.getEnd(), shifted.getStart())) {
            // Ende vom Konflikt überlappt Registrierungsstart -> beginne später
            shifted.setStart(earlier.getEnd());
        }

         /*
        Konflikt:           |       |
        Registrierung:  |       |
        Ergebnis:       |   |
        */
        if (isBefore(shifted.getStart(), later.getStart()) && isAfter(shifted.getEnd(), later.getStart())) {
            // Anfang vom Konflikt überlappt Registrierungsstart -> ende früher
            shifted.setEnd(later.getStart());
        }

        if (shifted.getStart().isBefore(shifted.getEnd())) {
            return shifted;
        }


        return registration;
    }

    private static boolean isBefore(LocalDateTime isBefore, LocalDateTime localDateTime) {
        return isBefore.isBefore(localDateTime) || isBefore.isEqual(localDateTime);
    }

    private static boolean isAfter(LocalDateTime isAfter, LocalDateTime localDateTime) {
        return isAfter.isAfter(localDateTime) || isAfter.isEqual(localDateTime);
    }
}
