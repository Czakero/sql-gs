package com.sqlcsv.sqlcsv.todo;

import com.sqlcsv.sqlcsv.model.Table;

import java.util.List;
import java.util.Optional;

public interface TableRepositoryInterface {
    Table save(Table file);
    Optional<Table> getById(int id);
    List<Table> getAll();
    boolean deleteById(int id);
    List<Table> findByName(String name);

}
