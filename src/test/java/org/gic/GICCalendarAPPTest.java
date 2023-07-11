package org.gic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class GICCalendarAPPTest {

    GICCalendarAPP gicCalendarAPP;

    GICCalendar mockedCalendar;
    HolidayData mockedHolidayData;

    @BeforeEach
    public void setup()
    {
        mockedCalendar = Mockito.mock(GICCalendar.class);
        mockedHolidayData = Mockito.mock(HolidayData.class);
        gicCalendarAPP = new GICCalendarAPP(mockedCalendar);

        Set<String> countries = new HashSet<>();
        countries.add("SG");
        countries.add("HK");
        when(mockedCalendar.getHolidayData()).thenReturn(mockedHolidayData);
        when(mockedHolidayData.getHolidayCountries()).thenReturn(Optional.of(countries));

    }

    @Test
    void shouldSystemOutWhenAppIsStartedWithNoPredefinedCountryList() throws Exception {
        when(mockedCalendar.getHolidayData()).thenReturn(mockedHolidayData);
        when(mockedHolidayData.getHolidayCountries()).thenReturn(Optional.empty());


        String text = tapSystemOut(() -> {
            gicCalendarAPP.start();
        });
        assertEquals("No predefined holiday list provided for any country\n", text);
    }

        @Test
    void shouldSystemOutMessageWhenInvalidCountryIsSelected() throws Exception {
        withTextFromSystemIn("3")
                .execute(() -> {

                    String text = tapSystemOutNormalized(() -> {
                        gicCalendarAPP.start();
                    });
                    assertEquals(gicCalendarAPP.WELCOMESTRING + "\n"+
                            gicCalendarAPP.CHOOSE_A_COUNTRY + "\n"+
                            "[1] HK\n[2] SG\n"+
                            "Please enter a valid country\n", text);
                });
    }

    @Test
    void shouldSystemOutMessageWhenUnrecoginsedOptionIsGiven() throws Exception {
        withTextFromSystemIn("1", "3")
                .execute(() -> {
                    String text = tapSystemOut(() -> {
                        gicCalendarAPP.start();
                    });
                    assertEquals(gicCalendarAPP.WELCOMESTRING +"\n"+
                                    gicCalendarAPP.CHOOSE_A_COUNTRY +"\n"+
                                    "[1] HK\n[2] SG\n" +
                                    gicCalendarAPP.CHOOSE_AN_ACTION +"\n"+
                            gicCalendarAPP.ACTIONS.get(0) + "\n" +
                            gicCalendarAPP.ACTIONS.get(1) + "\n" +
                            "Unrecognised option\n", text);
                });
    }

    @Test
    void shouldInvokeCheckDateForOption1WithSelectedCountry() throws Exception {
        when(mockedCalendar.checkDate(any(),anyString())).thenReturn(GICCalendar.DateClassifier.WEEKEND);
        withTextFromSystemIn("1", "1", "20230709", "#")
                .execute(() -> {
                    gicCalendarAPP.start();

                    verify(mockedCalendar, times(1)).checkDate(LocalDate.of(2023,07,9), "HK");
                });
    }

    @Test
    void shouldInvokeGetNextBusinessDayForOption2WithSelectedCountry() throws Exception {
        when(mockedCalendar.getNextBusinessDay(any(),anyString())).thenReturn(LocalDate.of(2023,01,26));
        withTextFromSystemIn("1", "2", "20230120", "#")
         .execute(() -> {
                    gicCalendarAPP.start();

                    verify(mockedCalendar, times(1)).getNextBusinessDay(LocalDate.of(2023,01,20), "HK");
                });
    }

    @Test
    void shouldInvokeCheckDateTwiceForOption1WithSelectedCountry() throws Exception {
        when(mockedCalendar.checkDate(any(),anyString())).thenReturn(GICCalendar.DateClassifier.WEEKEND);
        withTextFromSystemIn("1", "1", "20230709", "20230709", "#")
                .execute(() -> {
                    gicCalendarAPP.start();

                    verify(mockedCalendar, times(2)).checkDate(LocalDate.of(2023,07,9), "HK");
                });
    }

    @Test
    void shouldInvokeGetNextBusinessDayTwiceForOption2WithSelectedCountry() throws Exception {
        when(mockedCalendar.getNextBusinessDay(any(),anyString())).thenReturn(LocalDate.of(2023,01,26));
        withTextFromSystemIn("1", "2", "20230120","20230120", "#")
                .execute(() -> {
                    gicCalendarAPP.start();

                    verify(mockedCalendar, times(2)).getNextBusinessDay(LocalDate.of(2023,01,20), "HK");
                });
    }

}