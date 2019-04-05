package com.sqlcsv.sqlcsv.interfaces;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.enums.SQLKeywords;

import java.util.List;
import java.util.Map;

public interface IQueryExecutor {
    String[][] executeQuery(String[][] queriedSheet, Map<SQLKeywords, List<String>> parsedQuery) throws ParseQueryException;
}
