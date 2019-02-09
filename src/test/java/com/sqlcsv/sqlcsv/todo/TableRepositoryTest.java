package com.sqlcsv.sqlcsv.todo;

import com.sqlcsv.sqlcsv.model.Row;
import com.sqlcsv.sqlcsv.model.Table;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableRepositoryTest {

    @Test
    void testAddingTable_RepoShouldHaveItOnFirstIndexOfTheList() {
        TableRepository repo = new TableRepository();
        List<Row> tableData = new ArrayList<>();
        List<String> columnData = new ArrayList<>();
        List<String> entityData = new ArrayList<>();
        columnData.add("columnOne");
        columnData.add("ColumnTwo");
        entityData.add("columnOneValue");
        entityData.add("columnTwoValue");
        Row columnRow = new Row(columnData);
        Row entityRow = new Row(entityData);
        tableData.add(columnRow);
        tableData.add(entityRow);
        Table newTable = new Table("testFile.csv", tableData);

        repo.save(newTable);
        assertTrue(repo.getById(0).isPresent());
        Table savedTable = repo.getById(0).get();

        assertEquals(newTable, savedTable);
    }

}