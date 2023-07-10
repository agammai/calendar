package org.gic;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class HolidayCalendarTest {

    HolidayCalendar holidayCalendar = new HolidayCalendar("holidaystestdata");
    @Test
    void shouldReturnEmptyWhenHolidayListNotFoundForCountry() {
        assertTrue(holidayCalendar.getHolidays("HK").isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenHolidaysAreEmptyForCountry() {
        assertTrue(holidayCalendar.getHolidays("EmptySG").isEmpty());
    }

    @Test
    void shouldReturnHolidaysWhenHolidaysAreFound() {
        Optional<TreeSet<Holiday>> optionalHolidays = holidayCalendar.getHolidays("SG");
        TreeSet<Holiday> holidays = optionalHolidays.get();
        assertFalse(holidays.isEmpty());
        assertEquals(holidays.size(), 2);
    }

    @Test
    void shouldReturnOnlyValidHolidaysWhenInvalidDatesArePresent() {
        Optional<TreeSet<Holiday>> optionalHolidays = holidayCalendar.getHolidays("SG_Exclude_InvalidDates");
        TreeSet<Holiday> holidays = optionalHolidays.get();
        assertFalse(holidays.isEmpty());
        assertEquals(holidays.size(), 1);
    }
}