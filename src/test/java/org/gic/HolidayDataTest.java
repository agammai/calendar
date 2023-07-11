package org.gic;

import org.gic.exception.HolidayParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class HolidayDataTest {
    @Nested
    class ResourceExistTest {

        HolidayData holidayData;
        @BeforeEach
        void setup() throws HolidayParserException {
            holidayData = new HolidayData("holidaystestdata");
        }

        @Test
        void shouldReturnEmptyWhenHolidayListNotFoundForCountry() {
            assertTrue(holidayData.getHolidays("HK").isEmpty());
        }

        @Test
        void shouldReturnEmptyWhenHolidaysAreEmptyForCountry() {
            assertTrue(holidayData.getHolidays("EmptySG").isEmpty());
        }

        @Test
        void shouldReturnHolidaysWhenHolidaysAreFound() {
            Optional<TreeSet<Holiday>> optionalHolidays = holidayData.getHolidays("SG");
            TreeSet<Holiday> holidays = optionalHolidays.get();
            assertFalse(holidays.isEmpty());
            assertEquals(holidays.size(), 5);
        }

        @Test
        void shouldReturnOnlyValidHolidaysWhenInvalidDatesArePresent() {
            Optional<TreeSet<Holiday>> optionalHolidays = holidayData.getHolidays("SG_Exclude_InvalidDates");
            TreeSet<Holiday> holidays = optionalHolidays.get();
            assertFalse(holidays.isEmpty());
            assertEquals(holidays.size(), 1);
        }
    }

    @Nested
    class ResourceNotExistTest
    {
        @Test
        void shouldReturnEmptyWhenResourceNotFound() throws HolidayParserException {
            assertFalse(new HolidayData("EmptyTestData").getHolidayCountries().isPresent());
        }

        @Test
        void shouldThrowExceptionWhenExceptionOccurAtFilesWalk() {

            try (MockedStatic mocked = mockStatic(Files.class)) {

                mocked.when(() -> Files.walk(any())).thenThrow(IOException.class);
                Assertions.assertThrows(HolidayParserException.class, () -> {
                    new HolidayData("directory_walk_error");
                });
            }
        }
    }
}