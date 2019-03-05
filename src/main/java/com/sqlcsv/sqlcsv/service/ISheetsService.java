package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.model.Table;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public interface ISheetsService {
    Table getSheetFromSpreadsheet(String spreadsheetId, String sheetName, String userId) throws IOException, GeneralSecurityException;
    List<String> getSheetsNamesFromSpreadsheet(String spreadsheetId, String userId) throws IOException, GeneralSecurityException;
    Map<String, String> getSheetsWithIds(String spreadsheetId, String userId) throws IOException, GeneralSecurityException;
}
