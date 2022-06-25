package de.sist.csc.web.calendar;

import de.sist.csc.calendar.Registration;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class ConflictCheckResponse {
    private final List<Registration> blockingRegistrations;
    private final List<Registration> shiftCausingRegistrations;
    private final List<Registration> unchangedRegistrations;
    private final List<Registration> shiftedRegistrations;
    private final List<Registration> blockedRegistrations;


    public static ConflictCheckResponse noConflicts(Registration registration) {
        return new ConflictCheckResponse(Collections.emptyList(), Collections.emptyList(), Collections.singletonList(registration), Collections.emptyList(), Collections.emptyList());
    }


    public static ConflictCheckResponse shifted(Registration registration, List<Registration> conflicts) {
        return new ConflictCheckResponse(Collections.emptyList(), conflicts, Collections.emptyList(), Collections.singletonList(registration), Collections.emptyList());
    }

    public static ConflictCheckResponse conflict(Registration registration, List<Registration> conflicts) {
        return new ConflictCheckResponse(conflicts, Collections.emptyList(), Collections.singletonList(registration), Collections.emptyList(), Collections.emptyList());
    }


}
