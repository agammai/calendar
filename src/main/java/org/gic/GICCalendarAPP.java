package org.gic;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GICCalendarAPP {
    private static final int OPT_DATECHECK = 1;
    private static final int OPT_GETNEXTBUSINESSDAY = 2;
    private static final String TERMINATIONSTRING = "#";
    static final String WELCOMESTRING = "Welcome to GIC Calendar Calculator";
    static final String CHOOSE_A_COUNTRY = "Please choose a country:";
    static final String CHOOSE_AN_ACTION = "Please choose an action:";
    static final List<String> ACTIONS = new ArrayList<>(){{
        add("[1] Check a date");
        add("[2] Calculate next business day");
    }
    };
    static final String DATECHECK = "Please enter a date to check in yyyyMMdd format";
    static final String DATEINPUT = "Please enter a date in yyyyMMdd format";
    static final String CONTINUATIONSTRING = " or please enter # to end";

    private GICCalendar gicCalendar;

    public GICCalendarAPP(final GICCalendar gicCalendar) {

        this.gicCalendar = gicCalendar;
    }

    public void start()
    {
        HolidayData holidays = gicCalendar.getHolidayData();

        HashMap<Integer, String> indexCountryMap = new HashMap<>();

        if(!holidays.getHolidayCountries().isEmpty())
        {
            System.out.println(WELCOMESTRING);
            System.out.println(CHOOSE_A_COUNTRY);
            holidays.getHolidayCountries().get().forEach(withCounter((counter, country) -> {
                System.out.println("["+counter+"] "+country);
                indexCountryMap.put(counter, country);
            }));
            Scanner readInput = new Scanner(System.in);

            try {
                Integer selectedCountry = Integer.valueOf(readInput.nextLine());
                if (indexCountryMap.containsKey(selectedCountry)) {
                    String countryName = indexCountryMap.get(selectedCountry);

                    System.out.println(CHOOSE_AN_ACTION);
                    System.out.println(ACTIONS.get(0));
                    System.out.println(ACTIONS.get(1));

                    Integer selectedOption = Integer.valueOf(readInput.nextLine());


                    switch (selectedOption) {
                        case OPT_DATECHECK:

                            System.out.println(DATECHECK);
                            String userDate = readInput.nextLine();
                            while (!userDate.contains(TERMINATIONSTRING)) {
                                LocalDate userFormattedDate = LocalDate.parse(userDate, GICCalendarConstant.DATE_TIME_FORMATTER);
                                GICCalendar.DateClassifier dateClassifier = gicCalendar.checkDate(userFormattedDate, countryName);
                                System.out.println(userDate + " is a " + dateClassifier.displayName);
                                System.out.println(DATECHECK + CONTINUATIONSTRING);
                                userDate = readInput.nextLine();
                            }
                            break;

                        case OPT_GETNEXTBUSINESSDAY:
                            System.out.println(DATEINPUT);
                            String businessDateQueryInput = readInput.nextLine();
                            while (!businessDateQueryInput.contains(TERMINATIONSTRING)) {
                                LocalDate bDQueryFormattedInput = LocalDate.parse(businessDateQueryInput, GICCalendarConstant.DATE_TIME_FORMATTER);
                                LocalDate nextBusinessDay = gicCalendar.getNextBusinessDay(bDQueryFormattedInput, countryName);
                                System.out.println("The next business day is " + nextBusinessDay.format(GICCalendarConstant.DATE_TIME_FORMATTER));
                                System.out.println(DATEINPUT + CONTINUATIONSTRING);
                                businessDateQueryInput = readInput.nextLine();
                            }
                            break;

                        default:
                            System.out.println("Unrecognised option");
                            break;
                    }
                } else {
                    System.out.println("Please enter a valid country");
                }
            } catch (DateTimeParseException | NumberFormatException e) {
                throw new RuntimeException("Unexpected format. Terminating the application");
            }

        }
        else
        {
            System.out.println("No predefined holiday list provided for any country");
        }
    }

    private static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(1);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }
}
