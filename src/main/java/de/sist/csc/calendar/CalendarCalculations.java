package de.sist.csc.calendar;

import com.google.common.collect.Iterables;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("RedundantIfStatement")
public class CalendarCalculations {

    private CalendarCalculations() {
    }

    public static boolean isOverlapping(Registration a, Registration b) {
        if (b.getStart().isBefore(a.getStart()) && b.getEnd().isAfter(a.getStart())) {
            return true;
        }
        if (b.getStart().isBefore(a.getEnd()) && b.getEnd().isAfter(a.getStart())) {
            return true;
        }
        return false;
    }

    public static Registration tryShiftRegistration(Registration registration, List<Registration> conflicts) {
        final Registration shifted = new Registration(registration.getId(), registration.getStart(), registration.getEnd(), registration.getUsername(), registration.getTitle(), registration.getText());
        conflicts = new ArrayList<>(conflicts);
        conflicts.sort(Comparator.comparing(Registration::getStart));
        final Registration earlier = conflicts.get(0);
        final Registration later = Iterables.getLast(conflicts);

        /*
        Nicht möglich:
        Konflikt:       |               |
        Reservierung:      |       |
        */

        if (isBefore(earlier.getStart(), shifted.getStart()) && isAfter(earlier.getEnd(), shifted.getEnd())) {
            return shifted;
        }
        if (isBefore(later.getStart(), shifted.getStart()) && isAfter(later.getEnd(), shifted.getEnd())) {
            return shifted;
        }

        /*
        Konflikt:       |       |
        Reservierung:      |       |
        Ergebnis:               |   |
        */
        if (isBefore(earlier.getStart(), shifted.getStart()) && isAfter(earlier.getEnd(), shifted.getStart())) {
            // Ende vom Konflikt überlappt Reservierungsstart -> beginne später
            shifted.setStart(earlier.getEnd());
        }

         /*
        Konflikt:           |       |
        Reservierung:  |       |
        Ergebnis:       |   |
        */
        if (isBefore(shifted.getStart(), later.getStart()) && isAfter(shifted.getEnd(), later.getStart())) {
            // Anfang vom Konflikt überlappt Reservierungsstart -> ende früher
            shifted.setEnd(later.getStart());
        }

        if (shifted.getStart().isBefore(shifted.getEnd())) {
            return shifted;
        }


        return registration;
    }

    private static boolean isBefore(Instant isBefore, Instant localDateTime) {
        return isBefore.isBefore(localDateTime) || isBefore.equals(localDateTime);
    }

    private static boolean isAfter(Instant isAfter, Instant localDateTime) {
        return isAfter.isAfter(localDateTime) || isAfter.equals(localDateTime);
    }
}
