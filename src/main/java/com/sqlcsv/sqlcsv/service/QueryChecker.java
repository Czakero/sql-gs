package com.sqlcsv.sqlcsv.service;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.TokenMgrError;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Component
public class QueryChecker implements IQueryChecker {

    @Override
    public void checkQuery(String query) throws ParseQueryException {
        basicChecks(query);
        try {
            parserManager.parse(new StringReader(query));
        } catch (JSQLParserException | TokenMgrError e) {
            throw new ParseQueryException("Invalid query!");
        }
    }

    private void basicChecks(String query) throws ParseQueryException {
        if (query.length() == 0) {
            throw new ParseQueryException("Query does not exist :o");
        } else if (!query.endsWith(";")) {
            throw new ParseQueryException("Query not closed properly with semicolon!");
        } else if (queryContainsMoreThanOneSemicolon(query)) {
            throw new ParseQueryException("Too much semicolons in query!");
        }
    }

    private boolean queryContainsMoreThanOneSemicolon(String query) {
        return query
                .chars()
                .mapToObj(c -> (char) c)
                .filter(character -> character.equals(';'))
                .count() > 1;
    }
}
