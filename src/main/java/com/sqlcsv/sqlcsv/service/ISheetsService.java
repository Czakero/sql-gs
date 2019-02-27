package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.model.Table;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface ISheetsService {
    Table getSheetFromSpreadsheet(String spreadsheetId, String sheetName, String userId) throws IOException, GeneralSecurityException;
    List<String> getSheetsNamesFromSpreadsheet(String spreadsheetId, String userId) throws IOException, GeneralSecurityException;
}
