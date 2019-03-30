package com.sqlcsv.sqlcsv.interfaces;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IQueryService {
    String[][] handleQuery(String query, String spreadsheetId, String userId) throws ParseQueryException, IOException, GeneralSecurityException;
}
