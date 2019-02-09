package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.model.Row;
import com.sqlcsv.sqlcsv.model.Table;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SheetsParser {

    public Table getTableFromSheetValues(String sheetName, List<String[]> sheetValues) {
        return new Table(sheetName, sheetValues.stream()
                                                    .map(array -> new Row(Arrays.stream(array)
                                                    .collect(Collectors.toList())))
                                                .collect(Collectors.toList()));

//            result = sheets
//                    .stream()
//                    .map(sheet ->
//                            new Table(sheet.getProperties().getTitle(),
//                                    sheet.getData().stream().map(gridData ->
//                                            new Row(gridData.getRowData().stream().map(rowData ->
//                                                    rowData.getValues().stream().map(CellData::getFormattedValue)
//                                                            .collect(Collectors.toList()))
//                                                    .collect(Collectors.toList())
//                                                    .stream()
//                                                    .flatMap(Collection::stream)
//                                                    .collect(Collectors.toList())
//                                            )
//                                    ).collect(Collectors.toList())
//                            )
//                    ).collect(Collectors.toList());

//        for (Table table : result) {
//            System.out.println("Sheet name: " + table.getName());
//            for (Row row : table.getRows()) {
//                System.out.println("Sheet rows: " + row.getData());
//            }
//        }
    }
}
