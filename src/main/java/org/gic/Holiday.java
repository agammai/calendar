package org.gic;

import java.time.LocalDate;
import java.util.TreeSet;

public class Holiday implements Comparable<Holiday>{
    private LocalDate localDate;

    public Holiday(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    @Override
    public int compareTo(Holiday other) {
        return this.localDate.compareTo(other.localDate);
    }
}
