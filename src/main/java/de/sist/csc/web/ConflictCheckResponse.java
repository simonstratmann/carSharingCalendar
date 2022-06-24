// (C) 2022 PPI AG
package de.sist.csc.web;

import de.sist.csc.model.Registration;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class ConflictCheckResponse {
    private final List<Registration> conflicts;
    private final Registration registration;
    private final boolean shifted;

    public static ConflictCheckResponse noConflicts(Registration registration) {
        return new ConflictCheckResponse(Collections.emptyList(), registration, false);
    }

    public static ConflictCheckResponse shifted(Registration registration, List<Registration> registrations) {
        return new ConflictCheckResponse(registrations, registration, true);
    }

    public static ConflictCheckResponse conflict(Registration registration, List<Registration> registrations) {
        return new ConflictCheckResponse(registrations, registration, false);
    }


}
