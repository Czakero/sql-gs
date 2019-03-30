package com.sqlcsv.sqlcsv.service.queryhandlers;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.enums.SQLKeywords;
import com.sqlcsv.sqlcsv.interfaces.IQueryMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QueryMapper implements IQueryMapper {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Map<SQLKeywords, List<String>> mapQuery(String query) throws ParseQueryException {
        Map<SQLKeywords, List<String>> result = new EnumMap<>(SQLKeywords.class);
        String keyword = "";
        for (String word : query.split(" ")) {
            if (SQLKeywords.contains(word.toUpperCase())) {
                keyword = word;
                result.put(SQLKeywords.getByName(keyword).get(), new ArrayList<>());
            } else if (NOT_SUPPORTED_KEYWORDS.contains(word.toUpperCase())) {
                throw new ParseQueryException(word.toUpperCase() + " statement is not supported now! Maybe in the future.");
            } else {
                result.get(SQLKeywords.getByName(keyword).get()).add(word.replaceAll(",", "").replaceAll(";", "").replaceAll("'", ""));
            }
        }
        return result;
    }
}
