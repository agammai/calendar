package org.gic;

import org.gic.exception.HolidayParserException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GICCalendarTest {

    GICCalendar calendar;
    HolidayData mockedHolidayData;

    @BeforeAll
    public void oneTimeSetup() throws HolidayParserException {
        mockedHolidayData = Mockito.mock(HolidayData.class);
        calendar = new GICCalendar(mockedHolidayData);
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
        TreeSet holidayList = new TreeSet<>();
        holidayList.add(new Holiday(LocalDate.of(2023, 01, 02)));
        when(mockedHolidayData.getHolidays("SG")).thenReturn(Optional.of(holidayList));
        assertEquals(calendar.checkDate(LocalDate.of(2023,01,02), "SG"), GICCalendar.DateClassifier.PUBLICHOLIDAY);
    }

    @Test
    void shouldReturnPublicHolidayGivenDateIsAHolidayAndWeekend() {
        TreeSet holidayList = new TreeSet<>();
        holidayList.add(new Holiday(LocalDate.of(2023, 07, 15)));
        when(mockedHolidayData.getHolidays("SG")).thenReturn(Optional.of(holidayList));
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,15), "SG"), GICCalendar.DateClassifier.PUBLICHOLIDAY);
    }

    @Test
    void shouldReturnMondayGivenFriday()
    {
        assertEquals(calendar.getNextBusinessDay(LocalDate.of(2023,07,07), "SG"),LocalDate.of(2023,07,10));
    }

    @Test
    void shouldReturnThursdayIfNextDayIsAPublicHoliday()
    {
        TreeSet holidayList = new TreeSet<>();
        holidayList.add(new Holiday(LocalDate.of(2023, 8, 9)));
        when(mockedHolidayData.getHolidays("SG")).thenReturn(Optional.of(holidayList));
        assertEquals(calendar.getNextBusinessDay(LocalDate.of(2023,8,8), "SG"),LocalDate.of(2023,8,10));
    }

}