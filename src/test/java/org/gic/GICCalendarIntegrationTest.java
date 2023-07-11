package org.gic;

import org.gic.exception.HolidayParserException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GICCalendarIntegrationTest {

    @Test
    void shouldReturnMondayForThursdayIfNextDayIsAPublicHoliday() throws HolidayParserException {
        HolidayData testHolidayData = new HolidayData("holidaystestdata");
        GICCalendar calendar = new GICCalendar(testHolidayData);
        assertEquals(calendar.getNextBusinessDay(LocalDate.of(2023,06,01), "SG"),LocalDate.of(2023,06,05));
    }
}
