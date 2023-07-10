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
    void shouldReturnWeekEndIfDateIsSaturaday() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,8)), GICCalendar.DateClassifier.WEEKEND);
    }

    @Test
    void shouldReturnWeekEndIfDateIsSunday() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,9)), GICCalendar.DateClassifier.WEEKEND);
    }

    @Test
    void shouldReturnWorkingDayIfDateIsNotWeekEnd() {
        assertEquals(calendar.checkDate(LocalDate.of(2023,07,10)), GICCalendar.DateClassifier.WORKINGDAY);
    }
}