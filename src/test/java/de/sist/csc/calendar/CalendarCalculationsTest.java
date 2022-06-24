package de.sist.csc.calendar;

import de.sist.csc.model.Registration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith({MockitoExtension.class})
class CalendarCalculationsTest {

    @Test
    void isOverlapping() {
        final Registration r20_10_20_18 = CalendarCalculationsTest.getRegistration(20, 10, 20, 18, "", "");
        final Registration r20_10_20_12 = CalendarCalculationsTest.getRegistration(20, 10, 20, 12, "", "");
        final Registration r20_14_20_18 = CalendarCalculationsTest.getRegistration(20, 14, 20, 18, "", "");
        final Registration r19_14_19_18 = CalendarCalculationsTest.getRegistration(19, 14, 19, 18, "", "");
        final Registration r19_14_20_10 = CalendarCalculationsTest.getRegistration(19, 14, 20, 10, "", "");
        final Registration r20_14_20_11 = CalendarCalculationsTest.getRegistration(20, 14, 20, 11, "", "");
        final Registration r10_14_30_11 = CalendarCalculationsTest.getRegistration(10, 14, 30, 11, "", "");
        Assertions.assertThat(CalendarCalculations.isOverlapping(r20_10_20_18, r20_10_20_12)).isTrue();
        Assertions.assertThat(CalendarCalculations.isOverlapping(r20_10_20_18, r20_14_20_18)).isTrue();
        Assertions.assertThat(CalendarCalculations.isOverlapping(r20_10_20_18, r19_14_19_18)).isFalse();
        Assertions.assertThat(CalendarCalculations.isOverlapping(r20_10_20_18, r19_14_20_10)).isFalse();
        Assertions.assertThat(CalendarCalculations.isOverlapping(r20_10_20_18, r20_14_20_11)).isTrue();
        Assertions.assertThat(CalendarCalculations.isOverlapping(r20_10_20_18, r10_14_30_11)).isTrue();
    }

    @Test
    void shouldShiftStartLater() {
        final Registration c20_10_20_12 = CalendarCalculationsTest.getRegistration(20, 10, 20, 12, "", "");
        final Registration r20_10_20_18 = CalendarCalculationsTest.getRegistration(20, 10, 20, 18, "", "");
        final Registration shifted = CalendarCalculations.tryShiftRegistration(r20_10_20_18, Collections.singletonList(c20_10_20_12));

        Assertions.assertThat(shifted.getStart()).isEqualTo(getLocalDateTime(20, 12));
        Assertions.assertThat(shifted.getEnd()).isEqualTo(getLocalDateTime(20, 18));
    }

    @Test
    void shouldShiftEndEarlier() {
        final Registration r20_10_20_16 = CalendarCalculationsTest.getRegistration(20, 10, 20, 16, "", "");
        final Registration c20_14_20_18 = CalendarCalculationsTest.getRegistration(20, 14, 20, 18, "", "");
        final Registration shifted = CalendarCalculations.tryShiftRegistration(r20_10_20_16, Collections.singletonList(c20_14_20_18));

        Assertions.assertThat(shifted.getStart()).isEqualTo(getLocalDateTime(20, 10));
        Assertions.assertThat(shifted.getEnd()).isEqualTo(getLocalDateTime(20, 14));
    }

    @Test
    void shouldShiftBoth() {
        /*
        Conflict:       |     |
        Conflict:                           |       |
        Registrierung:     |                  |
        Ergebnis:             |             |
        */
        final Registration r10_16 = CalendarCalculationsTest.getRegistration(20, 10, 20, 16, "", "");
        final Registration c10_11 = CalendarCalculationsTest.getRegistration(20, 10, 20, 11, "", "");
        final Registration c15_16 = CalendarCalculationsTest.getRegistration(20, 15, 20, 16, "", "");
        final Registration shifted = CalendarCalculations.tryShiftRegistration(r10_16, Arrays.asList(c10_11, c15_16));

        Assertions.assertThat(shifted.getStart()).isEqualTo(getLocalDateTime(20, 11));
        Assertions.assertThat(shifted.getEnd()).isEqualTo(getLocalDateTime(20, 15));
    }

    @Test
    void shouldNotShiftIfNotPossible() {
        final Registration r20_15_20_16 = CalendarCalculationsTest.getRegistration(20, 15, 20, 16, "", "");
        final Registration c20_14_20_18 = CalendarCalculationsTest.getRegistration(20, 14, 20, 18, "", "");
        final Registration shifted = CalendarCalculations.tryShiftRegistration(r20_15_20_16, Collections.singletonList(c20_14_20_18));

        Assertions.assertThat(shifted.getStart()).isEqualTo(getLocalDateTime(20, 15));
        Assertions.assertThat(shifted.getEnd()).isEqualTo(getLocalDateTime(20, 16));
    }


    private static Registration getRegistration(int fromDay, int fromHour, int toDay, int toHour, String title, String user) {
        return new Registration(getLocalDateTime(fromDay, fromHour), getLocalDateTime(toDay, toHour), user, title, "text");
    }

    private static LocalDateTime getLocalDateTime(int dayOfMonth, int hour) {
        return LocalDateTime.of(2022, Month.JUNE, dayOfMonth, hour, 0);
    }

}
