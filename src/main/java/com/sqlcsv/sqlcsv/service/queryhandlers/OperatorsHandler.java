package com.sqlcsv.sqlcsv.service.queryhandlers;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class OperatorsHandler {

    public int[] getIndexes(String[][] queriedSheet, int questionedColumnIndex, String whereOperator, String parameter) throws ParseQueryException {
        try {
            switch (whereOperator) {
                case "=":
                    return equalCase(queriedSheet, questionedColumnIndex, parameter);
                case "!=":
                    return notEqualCase(queriedSheet, questionedColumnIndex, parameter);
                case "<":
                    return lesserThanCase(queriedSheet, questionedColumnIndex, parameter);
                case ">":
                    return biggerThanCase(queriedSheet, questionedColumnIndex, parameter);
                case "<=":
                    return lesserOrEqualCase(queriedSheet, questionedColumnIndex, parameter);
                case ">=":
                    return biggerOrEqualCase(queriedSheet, questionedColumnIndex, parameter);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            throw new ParseQueryException("Provided parameter in where clause is not a number!");
        }
    }

    private int[] biggerOrEqualCase(String[][] queriedSheet, int questionedColumnIndex, String parameter) throws NumberFormatException {
        return IntStream
                .range(0 , queriedSheet.length)
                .filter(i -> Integer.valueOf(queriedSheet[i][questionedColumnIndex]) >= Integer.valueOf(parameter))
                .toArray();
    }

    private int[] lesserOrEqualCase(String[][] queriedSheet, int questionedColumnIndex, String parameter) throws NumberFormatException {
            return IntStream
                    .range(0 , queriedSheet.length)
                    .filter(i -> Integer.valueOf(queriedSheet[i][questionedColumnIndex]) <= Integer.valueOf(parameter))
                    .toArray();
    }

    private int[] biggerThanCase(String[][] queriedSheet, int questionedColumnIndex, String parameter) throws NumberFormatException {
            return IntStream
                    .range(0 , queriedSheet.length)
                    .filter(i -> Integer.valueOf(queriedSheet[i][questionedColumnIndex]) > Integer.valueOf(parameter))
                    .toArray();
    }

    private int[] lesserThanCase(String[][] queriedSheet, int questionedColumnIndex, String parameter) throws NumberFormatException {
            return IntStream
                    .range(0 , queriedSheet.length)
                    .filter(i -> Integer.valueOf(queriedSheet[i][questionedColumnIndex]) < Integer.valueOf(parameter))
                    .toArray();
    }

    private int[] notEqualCase(String[][] queriedSheet, int questionedColumnIndex, String parameter) {
        return IntStream
                .range(0 , queriedSheet.length)
                .filter(i -> !queriedSheet[i][questionedColumnIndex].equals(parameter))
                .toArray();
    }

    private int[] equalCase(String[][] queriedSheet, int questionedColumnIndex, String parameter) {
        return IntStream
                .range(0, queriedSheet.length)
                .filter(i -> queriedSheet[i][questionedColumnIndex].equals(parameter))
                .toArray();
    }
}
