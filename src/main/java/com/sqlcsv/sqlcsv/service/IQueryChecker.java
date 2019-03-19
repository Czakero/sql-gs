package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;

public interface IQueryChecker {
    CCJSqlParserManager parserManager = new CCJSqlParserManager();
    void checkQuery(String query) throws ParseQueryException;
}
