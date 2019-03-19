package com.sqlcsv.sqlcsv.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Table {
    private String name;
    private List<Row> rows;
}
