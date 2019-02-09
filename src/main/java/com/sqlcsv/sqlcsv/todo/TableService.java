package com.sqlcsv.sqlcsv.todo;

import com.sqlcsv.sqlcsv.model.Table;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService implements ITableService {


    @Override
    public List<String> getAllSpreadsheetsIds() {
        return null;
    }

    @Override
    public List<String> getAllSheetsNames(int spreadsheetId) {
        return null;
    }

    @Override
    public Table getTableById(int sheetId) {
        return null;
    }

    @Override
    public Table queryTheTable(String query) {
        return null;
    }
}