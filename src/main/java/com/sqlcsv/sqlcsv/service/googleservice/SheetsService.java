package com.sqlcsv.sqlcsv.service.googleservice;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.sqlcsv.sqlcsv.google.GoogleAuthorizationFlow;
import com.sqlcsv.sqlcsv.enums.ServicesEnum;
import com.sqlcsv.sqlcsv.interfaces.ISheetsService;
import com.sqlcsv.sqlcsv.service.parser.SheetsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

@Service
public class SheetsService implements ISheetsService {
    private SheetsParser sheetsParser;

    @Autowired
    public SheetsService(SheetsParser sheetsParser) {
        this.sheetsParser = sheetsParser;
    }

    public String[][] getSheetFromSpreadsheet(String spreadsheetId, String sheetName, String userId) throws IOException, GeneralSecurityException {
        Sheets sheetsService = (Sheets) GoogleAuthorizationFlow.getService(ServicesEnum.SHEETS, userId);
        return sheetsParser.parseSheetValues(obtainSheetContent(spreadsheetId, sheetName, sheetsService));
    }

    public List<String> getSheetsNamesFromSpreadsheet(String spreadsheetId, String userId) throws IOException, GeneralSecurityException {
        Sheets sheetsService = (Sheets) GoogleAuthorizationFlow.getService(ServicesEnum.SHEETS, userId);
        Spreadsheet spreadsheet = Objects.requireNonNull(sheetsService).spreadsheets().get(spreadsheetId).execute();
        return sheetsParser.parseSheetNamesFromSpreadSheet(spreadsheet.getSheets());
    }

    private ValueRange obtainSheetContent(String spreadsheetId, String sheetName, Sheets sheetsService) throws IOException {
        return Objects.requireNonNull(sheetsService)
                .spreadsheets()
                .values()
                .get(spreadsheetId, sheetName)
                .execute();
    }
}
