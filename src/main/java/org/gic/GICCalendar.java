package org.gic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.function.Predicate;

public class GICCalendar {

    private HolidayCalendar holidayCalendar;

    public GICCalendar(final HolidayCalendar holidayCalendar) {

        this.holidayCalendar = holidayCalendar;
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

    private enum DatePredicate implements Predicate<LocalDate> {

        WEEKEND {
            @Override
            public boolean test(LocalDate date) {

                DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
                return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
            }
        }
    }

    public DateClassifier checkDate(LocalDate localDate)
    {
        DateClassifier dateClassifier = DateClassifier.WORKINGDAY;
        if(DatePredicate.WEEKEND.test(localDate))
        {
            dateClassifier =  DateClassifier.WEEKEND;
        }
        return dateClassifier;
    }
}
