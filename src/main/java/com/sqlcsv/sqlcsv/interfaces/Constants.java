package com.sqlcsv.sqlcsv.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Constants {
    int COLUMN_INDEX_IN_WHERE_CLAUSE = 0;
    int OPERATOR_INDEX_IN_WHERE_CLAUSE = 1;
    int PARAMETER_INDEX_IN_WHERE_CLAUSE = 2;
    List<String> OPERATORS = new ArrayList<>(Arrays.asList(">", "<", "=", "!=", ">=", "<="));
}
