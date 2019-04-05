package com.sqlcsv.sqlcsv.service.queryhandlers;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.enums.SQLKeywords;
import com.sqlcsv.sqlcsv.interfaces.Constants;
import com.sqlcsv.sqlcsv.interfaces.IQueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QueryExecutor implements IQueryExecutor, Constants {
    private FunctionsContainer functionsContainer;

    @Autowired
    public QueryExecutor(FunctionsContainer functionsContainer) {
        this.functionsContainer = functionsContainer;
    }

    @Override
    public String[][] executeQuery(String[][] queriedSheet, Map<SQLKeywords, List<String>> parsedQuery) throws ParseQueryException {
        String[][] result = queriedSheet;
        for (SQLKeywords keyword : parsedQuery.keySet()) {
            result = handleClause(result, parsedQuery, keyword);
        }
        return result;
    }

    private String[][] handleClause(String[][] queriedSheet, Map<SQLKeywords, List<String>> parsedQuery, SQLKeywords keyword) {
        return functionsContainer.getFunction(keyword).apply(queriedSheet, parsedQuery.get(keyword));
    }
}
