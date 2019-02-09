package com.sqlcsv.sqlcsv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public void handleError(HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
