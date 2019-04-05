package com.sqlcsv.sqlcsv.interfaces;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface ISheetsService {
    String[][] getSheetFromSpreadsheet(String spreadsheetId, String sheetName, String userId) throws IOException, GeneralSecurityException;
    List<String> getSheetsNamesFromSpreadsheet(String spreadsheetId, String userId) throws IOException, GeneralSecurityException;
}
