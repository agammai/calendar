package org.gic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HolidayCalendar {

    private static final String EXCLUDEALLFILEEXTENSIONS_PATTERN = "(?<!^)[.].*";
    private static final String EMPTYSTRING = "";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static Map<String, TreeSet<Holiday>> holidayCountryMap = new HashMap<>();

    public HolidayCalendar(final String resourceName) {
        loadHolidays(resourceName);
    }

    private enum DatePredicate implements Predicate<String> {
        VALID_DATE {
            @Override
            public boolean test(String date) {
                try {
                    LocalDate.parse(date, DATE_TIME_FORMATTER);
                } catch (DateTimeParseException e) {
                    return false;
                }
                return true;
            }
        }
    }
    private void loadHolidays(final String resourceName)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(resourceName);
        try(Stream<Path> paths = Files.walk(Paths.get(resource.toURI())))
        {
            paths.filter(Files::isRegularFile).forEach((file) -> {
                try {
                    TreeSet<Holiday> holidays = Files.readAllLines(file.toAbsolutePath(), StandardCharsets.UTF_8)
                            .stream()
                            .filter(DatePredicate.VALID_DATE)
                            .map(date -> new Holiday(LocalDate.parse(date, DATE_TIME_FORMATTER)))
                            .collect(Collectors.toCollection(TreeSet::new));
                    if(!holidays.isEmpty()) {
                        String fileName = file.getFileName().toString().replaceAll(EXCLUDEALLFILEEXTENSIONS_PATTERN, EMPTYSTRING);
                        holidayCountryMap.put(fileName, holidays);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TreeSet<Holiday>> getHolidays(final String countryName) {
        return Optional.ofNullable(holidayCountryMap != null ? holidayCountryMap.get(countryName) : null);
    }

}
