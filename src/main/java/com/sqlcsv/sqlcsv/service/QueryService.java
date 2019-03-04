package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class QueryService implements IQueryService {

    private ISheetsService sheetsService;

    @Autowired
    public QueryService(ISheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    @Override
    public Table handleQuery(String query) throws ParseQueryException {
        String[] splittedQuery = query.split(" ");
        System.out.println(Arrays.toString(splittedQuery));
        return null;
    }

    public static void main(String[] args) {
        QueryService queryService = new QueryService(new SheetsService(new SheetsParser()));
        queryService.handleQuery("SELECT * FROM dupa WHERE cipa like 'ciasna';");
    }
}
