package com.sqlcsv.sqlcsv.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
public class ExceptionsHandler {

    @ResponseStatus(value = HttpStatus.PERMANENT_REDIRECT)
    @ExceptionHandler({GeneralSecurityException.class, IOException.class})
    public void handleIOandGSEException(HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth");
    }
}
