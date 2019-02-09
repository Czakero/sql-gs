package com.sqlcsv.sqlcsv.todo;

import com.sqlcsv.sqlcsv.model.Table;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Repository
public class TableRepository implements TableRepositoryInterface {
    private List<Table> tables;

    public TableRepository() {
        this.tables = new ArrayList<>();
    }

    @Override
    public Table save(Table table) {
       boolean found = tables
               .stream()
               .anyMatch(x -> Objects.deepEquals(x, table));

       if (!found) {
           tables.add(table);
       }
       return table;
    }

    @Override
    public Optional<Table> getById(int id) {

        if (idIsNotInBound(tables, id)) {
            return Optional.empty();
        }
        return Optional.of(tables.get(id));
    }

    @Override
    public List<Table> getAll() {
        return tables;
    }

    @Override
    public boolean deleteById(int id) {
        if (idIsNotInBound(tables, id)) {
            return false;
        }
        tables.remove(id);
        return true;
    }

    @Override
    public List<Table> findByName(String name) {
        return TableRepositoryPredicates.filterCSVFiles(tables, TableRepositoryPredicates.isSameName(name));
    }

    private boolean idIsNotInBound(List<Table> tables, int id) {
        return id < 0 || id >= tables.size();
    }
}
