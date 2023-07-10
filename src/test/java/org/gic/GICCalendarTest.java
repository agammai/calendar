package org.gic;

import org.gic.exception.HolidayParserException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GICCalendarTest {

    GICCalendar calendar;

    @BeforeAll
    public void oneTimeSetup() throws HolidayParserException {
        HolidayCalendar testHolidayData = new HolidayCalendar("holidaystestdata");
        calendar = new GICCalendar(testHolidayData);
    }

    @Test
    void shouldReturnWeekEndIfDateIsSaturadayAndNotPublicHoliday() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,8), "SG"), GICCalendar.DateClassifier.WEEKEND);
    }

    @Test
    void shouldReturnWeekEndIfDateIsSundayAndNotPublicHoliday() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,9), "SG"), GICCalendar.DateClassifier.WEEKEND);
    }

    @Test
    void shouldReturnWorkingDayIfDateIsNotWeekEndAndPublicHoliday() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,10), "SG"), GICCalendar.DateClassifier.WORKINGDAY);
    }
    @Test
    void shouldReturnPublicHolidayGivenDateIsAHoliday() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,01,02), "SG"), GICCalendar.DateClassifier.PUBLICHOLIDAY);
    }

    @Test
    void shouldReturnPublicHolidayGivenDateIsAHolidayAndWeekend() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,15), "SG"), GICCalendar.DateClassifier.PUBLICHOLIDAY);
    }

    @Test
    void shouldReturnMondayGivenFriday()
    {
        assertEquals(calendar.getNextBusinessDay(LocalDate.of(2023,07,07), "SG"),LocalDate.of(2023,07,10));
    }

    @Test
    void shouldReturnMondayForThursdayIfNextDayIsAPublicHoliday()
    {
        assertEquals(calendar.getNextBusinessDay(LocalDate.of(2023,06,01), "SG"),LocalDate.of(2023,06,05));
    }

    @Test
    void shouldReturnThursdayIfNextDayIsAPublicHoliday()
    {
        assertEquals(calendar.getNextBusinessDay(LocalDate.of(2023,8,8), "SG"),LocalDate.of(2023,8,10));
    }

}