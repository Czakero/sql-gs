package com.sqlcsv.sqlcsv.enums;

import java.util.Arrays;
import java.util.Optional;

public enum SQLKeywords implements Comparable<SQLKeywords> {

    FROM("FROM"),
    WHERE("WHERE"),
    SELECT("SELECT"),
    OFFSET("OFFSET"),
    LIMIT("LIMIT");

    private final String name;

    SQLKeywords(String name) {
        this.name = name;
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
