package com.sqlcsv.sqlcsv.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Table {
    private String name;
    private List<Row> rows;
}
