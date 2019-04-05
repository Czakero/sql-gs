package com.sqlcsv.sqlcsv.service.queryhandlers;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.enums.SQLKeywords;
import com.sqlcsv.sqlcsv.interfaces.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;

@Component
public class FunctionsContainer implements Constants {
    private final Predicates predicates;
    private final OperatorsHandler operatorsHandler;
    private Map<SQLKeywords, BiFunction<String[][], List<String>, String[][]>> functions;

    @Autowired
    public FunctionsContainer(Predicates predicates, OperatorsHandler operatorsHandler) {
        this.predicates = predicates;
        this.operatorsHandler = operatorsHandler;
        init();
    }

    private void init() {
        this.functions = new EnumMap<>(SQLKeywords.class);
        functions.put(SQLKeywords.SELECT, getSelectFunction());
        functions.put(SQLKeywords.WHERE, getWhereFunction());
        functions.put(SQLKeywords.LIMIT, getLimitFunction());
        functions.put(SQLKeywords.OFFSET, getOffsetFunction());
    }

    public BiFunction<String[][], List<String>, String[][]> getFunction(SQLKeywords keyword) {
        return functions.get(keyword);
    }

    private BiFunction<String[][], List<String>, String[][]> getLimitFunction() {
        return  (result, limitParameter) -> {
            checkLimitFunctionParameters(limitParameter);
            int limit = tryToParseToInt(limitParameter.get(0));
            checkBounds(result, limit + 1);
            return handleLimit(result, limit);
        };
    }

    private String[][] handleLimit(String[][] result, int limit) {
        return Arrays.stream(result)
                .limit(limit + 1)
                .toArray(String[][]::new);
    }

    private BiFunction<String[][], List<String>, String[][]> getOffsetFunction() throws ParseQueryException {
        return (queriedSheet, offsetParameter) -> {
            checkLimitFunctionParameters(offsetParameter);
            int offset = tryToParseToInt(offsetParameter.get(0)) + 1;
            checkBounds(queriedSheet, offset);
            return handleOffset(queriedSheet, offset);
        };
    }

    private String[][] handleOffset(String[][] queriedSheet, int offset) {
        String[][] offsetResult = Arrays.stream(queriedSheet).skip(offset).toArray(String[][]::new);
        String[][] result = new String[offsetResult.length + 1][];
        result[0] = queriedSheet[0];
        System.arraycopy(offsetResult, 0, result, 1, offsetResult.length);
        return result;
    }

    private BiFunction<String[][], List<String>, String[][]> getSelectFunction() throws ParseQueryException {
        return (queriedSheet, selectParameters) -> {
            int[] columnsIndex = getColumnsIndex(queriedSheet, selectParameters);
            return Arrays.stream(queriedSheet)
                    .map(row -> Arrays.stream(columnsIndex)
                            .mapToObj(index -> row[index]).toArray(String[]::new))
                    .toArray(String[][]::new);
            };
    }

    private BiFunction<String[][], List<String>, String[][]> getWhereFunction() {
        return (queriedSheet, whereParameters) -> {
            check(whereParameters.size() % 3 != 0, "Where clause is incorrect!");

            String questionedColumnName = whereParameters.get(COLUMN_INDEX_IN_WHERE_CLAUSE);
            OptionalInt optionalOfIndex = getQuestionedColumnIndex(questionedColumnName, queriedSheet);

            if (optionalOfIndex.isPresent()) {
                int questionedColumnIndex = optionalOfIndex.getAsInt();
                Optional<String> optionalOfOperator = getOperator(whereParameters);
                if (optionalOfOperator.isPresent()) {
                    return executeWhereClause(queriedSheet, questionedColumnIndex, optionalOfOperator.get(), whereParameters.get(PARAMETER_INDEX_IN_WHERE_CLAUSE));
                } else {
                    throw new ParseQueryException("Operator in where clause doesn't exist or is incorrect!");
                }
            } else {
                throw new ParseQueryException(questionedColumnName + " column does not exist! Correct your query.");
            }
        };
    }

    private void check(boolean condition, String exceptionMessage) throws ParseQueryException {
        if (condition) {
            throw new ParseQueryException(exceptionMessage);
        }
    }

    private String[][] executeWhereClause(String[][] queriedSheet, int questionedColumnIndex, String whereOperator, String parameter) throws ParseQueryException {
        int[] resultRowsIndexes = operatorsHandler.getIndexes(queriedSheet, questionedColumnIndex, whereOperator, parameter);

        check(resultRowsIndexes == null, "Operator in where clause is not supported!");

        resultRowsIndexes = appendInfoRow(resultRowsIndexes);

        return stream(resultRowsIndexes)
                .mapToObj(i -> queriedSheet[i])
                .toArray(String[][]::new);
    }

    private int[] appendInfoRow(int[] resultRowsIndexes) {
        int[] result = new int[resultRowsIndexes.length + 1];
        System.arraycopy(resultRowsIndexes, 0, result, 1, result.length - 1);
        return result;
    }

    private void checkBounds(String[][] result, int value) throws ParseQueryException {
        check(value > result.length, "Offset/Limit not in bounds of result!");
    }

    private void checkLimitFunctionParameters(List<String> parameters) throws ParseQueryException {
        check(isEmptyOrHaveTooManyParameters(parameters), "Too many or none Offset/Limit parameters!");
    }

    private boolean isSelectEverything(List<String> selectParameters) {
        return selectParameters.stream().anyMatch(selectedColumn -> selectedColumn.equals("*"));
    }

    private boolean isEmptyOrHaveTooManyParameters(List<String> parameters) {
        return parameters.size() > 1 || parameters.size() == 0;
    }

    private int[] indexArrayWhenAsterisk(String[][] queriedSheet) {
        return IntStream.range(0, queriedSheet[0].length).toArray();
    }

    private Optional<String> getOperator(List<String> whereParameters) {
        return OPERATORS.stream()
                .filter(operator -> operator.equals(whereParameters.get(OPERATOR_INDEX_IN_WHERE_CLAUSE)))
                .findFirst();
    }

    private OptionalInt getQuestionedColumnIndex(String columnName, String[][] queriedSheet) {
        return IntStream.range(0, queriedSheet[0].length)
                .filter(i -> queriedSheet[0][i].equals(columnName))
                .findFirst();
    }

    private int[] getColumnsIndex(String[][] queriedSheet, List<String> selectParameters) {
        if (isSelectEverything(selectParameters)) {
            return indexArrayWhenAsterisk(queriedSheet);
        }
        return getSelectedIndexes(queriedSheet, selectParameters);
    }

    private int[] getSelectedIndexes(String[][] queriedSheet, List<String> selectParameters) {
        return selectParameters
                .stream()
                .map(selectedColumn -> getQuestionedColumnIndex(selectedColumn, queriedSheet))
                .filter(predicates.getSelectPredicate())
                .map(OptionalInt::getAsInt)
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private int tryToParseToInt(String value) throws ParseQueryException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException | NullPointerException e) {
            throw new ParseQueryException("Limit/Offset parameter is not a number!");
        }
    }
}
