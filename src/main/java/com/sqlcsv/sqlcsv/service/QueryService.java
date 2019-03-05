package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class QueryService implements IQueryService {

    private static final List<String> KEYWORDS = new ArrayList<>(Arrays.asList("SELECT", "FROM", "WHERE", "LIMIT", "OFFSET"));
    private ISheetsService sheetsService;

    @Autowired
    public QueryService(ISheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    @Override
    public Table handleQuery(String query, String spreadsheetId, String userId) throws ParseQueryException, IOException, GeneralSecurityException {
        Map<String, List<String>> parsedQuery = mapQuery(query);
        Table queriedSheet = sheetsService.getSheetFromSpreadsheet(spreadsheetId, parsedQuery.get("FROM").get(0), userId);
        if (queriedSheet == null) {
            throw new ParseQueryException("Provided sheet in FROM clause does not exist!");
        }
        System.out.println(parsedQuery.toString());
        System.out.println(queriedSheet);
        return null;
    }

    private Map<String, List<String>> mapQuery(String query) {
        String keyword = "";
        Map<String, List<String>> result = new HashMap<>();
        for (String word : query.split(" ")) {
            if (KEYWORDS.contains(word.toUpperCase())) {
                keyword = word;
                result.put(keyword, new ArrayList<>());
            } else {
                result.get(keyword).add(word.replaceAll(",", "").replaceAll(";", ""));
            }
        }
        try {
            result.replace("WHERE", Collections.singletonList(String.join(",", result.get("WHERE")).replaceAll(",", " ")));
        } catch (Exception e) {
            System.out.println("No where clause");
        }
        return result;
    }
}
