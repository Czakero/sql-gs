package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.model.SQLKeywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface IQueryExecutor {
    int COLUMN_INDEX_IN_WHERE_CLAUSE = 0;
    int OPERATOR_INDEX_IN_WHERE_CLAUSE = 1;
    int PARAMETER_INDEX_IN_WHERE_CLAUSE = 2;
    List<String> OPERATORS = new ArrayList<>(Arrays.asList(">", "<", "=", "!=", ">=", "<="));

    String[][] executeQuery(String[][] queriedSheet, Map<SQLKeywords, List<String>> parsedQuery) throws ParseQueryException;
}
