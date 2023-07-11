package org.gic;

import org.gic.exception.HolidayParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GICCalendarAPPIntegrationTest {

    HolidayData holidays;
    GICCalendar gicCalendar;

    GICCalendarAPP gicCalendarAPP;

    @BeforeEach
    public void setup() throws HolidayParserException {
        holidays = new HolidayData("holidays");
        gicCalendar = new GICCalendar(holidays);
        gicCalendarAPP = new GICCalendarAPP(gicCalendar);
    }

    @Test
    void shouldReturnWeekendForOption1WithSelectedCountry() throws Exception {
        withTextFromSystemIn("1", "1", "20230709", "#")
                .execute(() -> {
                    String text = tapSystemOut(() -> {
                        gicCalendarAPP.start();
                    });
                    assertTrue(text.contains("20230709 is a weekend"));
                });
    }

    @Test
    void shouldGetNextBusinessDayForOption2WithSelectedCountry() throws Exception {
        withTextFromSystemIn("1", "2", "20230120", "#")
                .execute(() -> {
                    String text = tapSystemOut(() -> {
                        gicCalendarAPP.start();
                    });
                    assertTrue(text.contains("The next business day is 20230126"));
                });
    }
}
