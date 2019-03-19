package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.model.Table;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IQueryService {
    String[][] handleQuery(String query, String spreadsheetId, String userId) throws ParseQueryException, IOException, GeneralSecurityException;
}
