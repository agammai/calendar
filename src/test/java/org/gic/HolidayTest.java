package org.gic;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HolidayTest {

    @Test
    void shouldReturn0IfDatesAreEqual() {

        LocalDate localDateJune16 = LocalDate.of(2023, 06, 16);
        Holiday june16 = new Holiday(localDateJune16);
        assertEquals(june16.compareTo(new Holiday(localDateJune16)), 0);
    }

    @Test
    void shouldReturnLessThan0IfComparedWithFollowingDate() {

        LocalDate localDateJune15 = LocalDate.of(2023, 06, 15);
        LocalDate localDateJune16 = LocalDate.of(2023, 06, 16);
        Holiday june16 = new Holiday(localDateJune15);
        assertTrue(june16.compareTo(new Holiday(localDateJune16)) < 0);
    }
    @Test
    void shouldReturnGreaterThan0IfComparedWithPreviousDate() {

        LocalDate localDateJune17 = LocalDate.of(2023, 06, 17);
        LocalDate localDateJune16 = LocalDate.of(2023, 06, 16);
        Holiday june16 = new Holiday(localDateJune17);
        assertTrue(june16.compareTo(new Holiday(localDateJune16)) > 0);
    }
}