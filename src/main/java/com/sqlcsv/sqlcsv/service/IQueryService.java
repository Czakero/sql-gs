package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.model.Table;

public interface IQueryService {
    Table handleQuery(String query) throws ParseQueryException;
}
