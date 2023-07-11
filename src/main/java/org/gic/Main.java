package org.gic;

import org.gic.exception.HolidayParserException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Opt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.

        HolidayData holidays = null;
        try {
            holidays = new HolidayData("holidays");
            GICCalendar gicCalendar = new GICCalendar(holidays);
            new GICCalendarAPP(gicCalendar).start();
        }
        catch (HolidayParserException e)
        {
            System.out.println("Error in parsing the pre defined holidays");
        }
        catch(RuntimeException e)
        {
            System.out.println(e.getMessage());
        }
    }
}