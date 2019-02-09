package com.sqlcsv.sqlcsv.todo;

import com.sqlcsv.sqlcsv.model.Table;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
@Component
public class TableRepositoryPredicates {
    public static Predicate<Table> isSameName(String name) {
        return p -> p.getName().equals(name);
    }

    public static List<Table> filterCSVFiles(List<Table> tables, Predicate<Table> predicate) {
        return tables.stream().filter(predicate).collect(Collectors.toList());
    }
}
