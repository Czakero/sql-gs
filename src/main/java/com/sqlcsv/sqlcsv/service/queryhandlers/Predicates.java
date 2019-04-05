package com.sqlcsv.sqlcsv.service.queryhandlers;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import org.springframework.stereotype.Component;

import java.util.OptionalInt;
import java.util.function.Predicate;

@Component
public class Predicates {

    public Predicate<OptionalInt> getSelectPredicate() throws ParseQueryException {
        return optionalInt -> {
            if (optionalInt.isPresent()) {
                return true;
            } else {
                throw new ParseQueryException("Some column provided in select clause does not exists!");
            }
        };
    }
}
