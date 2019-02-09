package com.sqlcsv.sqlcsv.todo;

import com.sqlcsv.sqlcsv.model.Table;

import java.io.IOException;
import java.util.List;

public interface ITableService {
    List<String> getAllSpreadsheetsIds() throws IOException;
    List<String> getAllSheetsNames(int spreadsheetId);
    Table getTableById(int sheetId);
    Table queryTheTable(String query);
}