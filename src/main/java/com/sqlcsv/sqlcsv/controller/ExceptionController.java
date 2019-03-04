package com.sqlcsv.sqlcsv.controller;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(value = HttpStatus.PERMANENT_REDIRECT)
    @ExceptionHandler(value = ParseQueryException.class)
    public String handleException(ParseQueryException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "parseQueryExceptionPage";
    }

    @ResponseStatus(value = HttpStatus.PERMANENT_REDIRECT)
    @ExceptionHandler({GeneralSecurityException.class, IOException.class})
    public void handleIOandGSEException(HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth");
    }
}
