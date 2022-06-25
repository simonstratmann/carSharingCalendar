package de.sist.csc.web.calendar;

import de.sist.csc.calendar.Calendar;
import de.sist.csc.calendar.Registration;
import de.sist.csc.calendar.RegistrationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith({MockitoExtension.class})
class CalendarWebTest {

    @Mock
    private RegistrationRepository repository;
    @Mock
    private Calendar calendar;

    @InjectMocks
    private CalendarWeb testee;

    @BeforeEach
    public void setUp() {
        mockResponse(Collections.emptyList());
    }

    private void mockResponse(List<Registration> registrations) {
        when(repository.findAllByStartAfterAndEndBefore(any(), any())).thenReturn(registrations);
        when(calendar.getRegistrationsStartEndBetween(any(), any())).thenReturn(registrations);
        when(calendar.getRegistrations()).thenReturn(registrations);
    }

    @Test
    public void shouldHandleRepeatingRegistrationsWithoutConflicts() throws Exception {
        final ConflictCheckResponse response = testee.checkForConflicting(getRegistration(10, 10, 10, 12), 3);
        Assertions.assertThat(response.getBlockedRegistrations()).isEmpty();
        Assertions.assertThat(response.getShiftedRegistrations()).isEmpty();
        Assertions.assertThat(response.getShiftCausingRegistrations()).isEmpty();
        Assertions.assertThat(response.getUnchangedRegistrations())
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        getRegistration(10, 10, 10, 12),
                        getRegistration(17, 10, 17, 12),
                        getRegistration(24, 10, 24, 12));
    }

    @Test
    public void shouldHandleNonRepeatingRegistrationsWithShifted() throws Exception {
        final Registration shiftCausing = getRegistration(10, 10, 10, 11);
        mockResponse(Collections.singletonList(shiftCausing));

        final Registration newRegistration = getRegistration(10, 10, 10, 12);
        final ConflictCheckResponse response = testee.checkForConflicting(newRegistration, 1);
        Assertions.assertThat(response.getBlockedRegistrations()).isEmpty();
        Assertions.assertThat(response.getShiftCausingRegistrations()).containsExactly(shiftCausing);
        Assertions.assertThat(response.getUnchangedRegistrations()).isEmpty();
        Assertions.assertThat(response.getShiftedRegistrations())
                .hasSize(1)
                //Start beginnt um 11 statt 10
                .containsExactly(getRegistration(10, 11, 10, 12));
    }

    @Test
    public void shouldHandleRepeatingRegistrationsWithOneShifted() throws Exception {
        final Registration shiftCausing = getRegistration(10, 10, 10, 11);
        mockResponse(Collections.singletonList(shiftCausing));

        final ConflictCheckResponse response = testee.checkForConflicting(getRegistration(10, 10, 10, 12), 3);
        Assertions.assertThat(response.getBlockedRegistrations()).isEmpty();
        Assertions.assertThat(response.getShiftCausingRegistrations()).containsExactly(shiftCausing);
        Assertions.assertThat(response.getUnchangedRegistrations())
                .containsExactly(
                        getRegistration(17, 10, 17, 12),
                        getRegistration(24, 10, 24, 12)
                );
        Assertions.assertThat(response.getShiftedRegistrations())
                .hasSize(1)
                .containsExactlyInAnyOrder(getRegistration(10, 11, 10, 12));
    }

    @Test
    public void shouldHandleRepeatingRegistrationsWithOneShiftedOneBlocked() throws Exception {
        final Registration shiftCausing = getRegistration(10, 10, 10, 11);
        final Registration blocking = getRegistration(17, 8, 17, 20);
        mockResponse(Arrays.asList(shiftCausing, blocking));

        final ConflictCheckResponse response = testee.checkForConflicting(getRegistration(10, 10, 10, 12), 3);
        Assertions.assertThat(response.getBlockedRegistrations()).containsExactly(getRegistration(17, 10, 17, 12));
        Assertions.assertThat(response.getBlockingRegistrations()).containsExactly(blocking);
        Assertions.assertThat(response.getShiftCausingRegistrations()).containsExactly(shiftCausing);
        Assertions.assertThat(response.getUnchangedRegistrations())
                .containsExactly(
                        getRegistration(24, 10, 24, 12)
                );
        Assertions.assertThat(response.getShiftedRegistrations())
                .hasSize(1)
                .containsExactlyInAnyOrder(getRegistration(10, 11, 10, 12));
    }

    private static Registration getRegistration(int fromDay, int fromHour, int toDay, int toHour) {
        return new Registration(1, getLocalDateTime(fromDay, fromHour), getLocalDateTime(toDay, toHour), "user", "title", "text");
    }

    private static Instant getLocalDateTime(int dayOfMonth, int hour) {
        return LocalDateTime.of(2022, Month.JUNE, dayOfMonth, hour, 0).toInstant(ZoneOffset.UTC);
    }

}
