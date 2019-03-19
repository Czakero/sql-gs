package com.sqlcsv.sqlcsv.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public enum SQLKeywords implements Comparable<SQLKeywords> {

    FROM("FROM", 0),
    WHERE("WHERE", 1),
    SELECT("SELECT", 2),
    LIMIT("LIMIT", 3),
    OFFSET("OFFSET", 4);

    private final String name;
    private final int placeInOrderOfExecution;

    SQLKeywords(String select, int i) {
        this.name = select;
        this.placeInOrderOfExecution = i;
    }


    public static Comparator<SQLKeywords> getComparator() {
        return Comparator.comparingInt(t -> t.placeInOrderOfExecution);
    }

    public static boolean contains(String keyword) {
        return Arrays.stream(SQLKeywords.values())
                .anyMatch(sqlKeyword -> sqlKeyword.name.equals(keyword));

    }

    public static Optional<SQLKeywords> getByName(String name) {
        return Arrays.stream(SQLKeywords.values())
                .filter(sqlKeyword -> sqlKeyword.name.equals(name))
                .findFirst();
    }

}
