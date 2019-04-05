package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.enums.SQLKeywords;
import com.sqlcsv.sqlcsv.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@Service
public class QueryService implements IQueryService {

    private ISheetsService sheetsService;
    private IQueryChecker queryChecker;
    private IQueryMapper queryMapper;
    private IQueryExecutor queryExecutor;

    @Autowired
    public QueryService(ISheetsService sheetsService, IQueryChecker queryChecker, IQueryMapper queryMapper, IQueryExecutor queryExecutor) {
        this.sheetsService = sheetsService;
        this.queryChecker = queryChecker;
        this.queryMapper = queryMapper;
        this.queryExecutor = queryExecutor;
    }

    @Override
    public String[][] handleQuery(String query, String spreadsheetId, String userId) throws ParseQueryException, IOException, GeneralSecurityException {
        queryChecker.checkQuery(query);
        Map<SQLKeywords, List<String>> parsedQuery = queryMapper.mapQuery(query);
        String[][] queriedSheet = sheetsService.getSheetFromSpreadsheet(spreadsheetId, parsedQuery.get(SQLKeywords.FROM).get(0), userId);
        parsedQuery.remove(SQLKeywords.FROM);
        return queryExecutor.executeQuery(queriedSheet, parsedQuery);
    }
}
