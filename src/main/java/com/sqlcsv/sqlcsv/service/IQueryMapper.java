package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.model.SQLKeywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface IQueryMapper {
    List<String> NOT_SUPPORTED_KEYWORDS = new ArrayList<>(Arrays.asList("JOIN", "AVG", "MAX", "UPDATE", "INSERT", "FUNCTION", "ALTER", "CREATE", "MIN", "ON", "DELETE", "TRUNCATE", "AND", "OR", "LIKE", "BETWEEN", "IN"));
    Map<SQLKeywords, List<String>> mapQuery(String query) throws ParseQueryException;
}
