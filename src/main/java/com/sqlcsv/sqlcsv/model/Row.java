package com.sqlcsv.sqlcsv.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Row {
    private List<String> data;
}
