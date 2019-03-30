package com.sqlcsv.sqlcsv.service.parser;

import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SheetsParser {
    public String[][] parseSheetValues(ValueRange queryOutput) {
        return queryOutput
                .getValues()
                .stream()
                .map(list -> list
                        .stream()
                        .map(Object::toString).toArray(String[]::new))
                .toArray(String[][]::new);

    }

    public List<String> parseSheetNamesFromSpreadSheet(List<Sheet> sheets) {
       return sheets
                .stream()
                .map(sheet -> sheet.getProperties().getTitle())
                .collect(Collectors.toList());
    }
}