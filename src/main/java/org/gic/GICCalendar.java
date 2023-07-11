package org.gic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Predicate;

public class GICCalendar {

    private HolidayData holidayData;

    public GICCalendar(final HolidayData holidayData) {

        this.holidayData = holidayData;
    }

    enum DateClassifier
    {
        WEEKEND("weekend"),
        WORKINGDAY("working day"),
        PUBLICHOLIDAY("public holiday");

        private final String displayName;
        DateClassifier(final String name)
        {
            displayName = name;
        }

    }
    public DateClassifier checkDate(final LocalDate localDate,final String countryName)
    {
        DateClassifier dateClassifier = DateClassifier.WORKINGDAY;
        if(DatePredicate.WEEKEND.test(localDate))
        {
            dateClassifier =  DateClassifier.WEEKEND;
        }
        Optional<TreeSet<Holiday>> optionalHolidays = holidayData.getHolidays(countryName);
        if(optionalHolidays.isPresent() && containsDate(optionalHolidays.get(), localDate))
        {
            dateClassifier =  DateClassifier.PUBLICHOLIDAY;
        }
        return dateClassifier;
    }

    public LocalDate getNextBusinessDay(final LocalDate localDate, final String countryName)
    {
        LocalDate nextBusinessDay = localDate;
        do {
            nextBusinessDay = nextBusinessDay.plusDays(1);
        }while (checkDate(nextBusinessDay, countryName) == DateClassifier.WEEKEND || checkDate(nextBusinessDay, countryName) == DateClassifier.PUBLICHOLIDAY);
        return nextBusinessDay;
    }

    private enum DatePredicate implements Predicate<LocalDate> {

        WEEKEND {
            @Override
            public boolean test(LocalDate date) {

                DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
                return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
            }
        }
    }

    private boolean containsDate(final TreeSet<Holiday> holidays, final LocalDate localDate){
        return holidays.stream().anyMatch(o -> localDate.equals(o.getLocalDate()));
    }
}
