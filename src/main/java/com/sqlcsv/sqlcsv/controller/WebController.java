package com.sqlcsv.sqlcsv.controller;

import com.sqlcsv.sqlcsv.controller.exception.ParseQueryException;
import com.sqlcsv.sqlcsv.interfaces.IAuthService;
import com.sqlcsv.sqlcsv.interfaces.IDriveService;
import com.sqlcsv.sqlcsv.interfaces.IQueryService;
import com.sqlcsv.sqlcsv.interfaces.ISheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {
    private IDriveService driveService;
    private ISheetsService sheetsService;
    private IQueryService queryService;
    private IAuthService authService;

    @Autowired
    public WebController(IDriveService driveService, ISheetsService sheetsService, IQueryService queryService, IAuthService authService) {
        this.sheetsService = sheetsService;
        this.driveService = driveService;
        this.queryService = queryService;
        this.authService = authService;
    }

    @GetMapping("/")
    public void redirectTo(HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth");
    }

    @GetMapping("/auth")
    public void getAuthPage(HttpServletResponse response) throws IOException, GeneralSecurityException {
        response.sendRedirect(authService.createNewAuthorizationUrl());
    }

    @GetMapping("/query")
    public String doGet(HttpServletRequest request, Model model) throws IOException, GeneralSecurityException {
        String spreadsheetId =  request.getSession().getAttribute("spreadsheetId").toString();
        String userId = request.getSession().getAttribute("email").toString();
        List<String> sheetsNames = sheetsService.getSheetsNamesFromSpreadsheet(spreadsheetId, userId);
        model.addAttribute("sheetNames", sheetsNames);
        return "queryPage";
    }

    @PostMapping("/query")
    public String doPost(@RequestParam("query") String query, Model model, HttpServletRequest request) throws IOException, GeneralSecurityException, ParseQueryException {
        String userId = request.getSession().getAttribute("email").toString();
        String spreadsheetId = request.getSession().getAttribute("spreadsheetId").toString();
        List<String> sheetsNames = sheetsService.getSheetsNamesFromSpreadsheet(spreadsheetId, userId);
        model.addAttribute("table", queryService.handleQuery(query, spreadsheetId, userId));
        model.addAttribute("sheetNames", sheetsNames);
        return "resultPage";
    }

    @GetMapping("/choose")
    public String getChoosePage(Model model, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String userId = request.getSession().getAttribute("email").toString();
        Map<String, String> spreadsheets = driveService.getAllSpreadsheets(userId);
        model.addAttribute("spreadsheets", spreadsheets);
        return "choosePage";
    }

    @PostMapping("/choose")
    public void redirectToQueryPage(HttpServletResponse response, @RequestParam("spreadsheetId") String spreadsheetId, HttpServletRequest request) throws IOException {
        request.getSession().setAttribute("spreadsheetId", spreadsheetId);
        response.sendRedirect("/query");
    }

    @GetMapping("/callback")
    public void getToken(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
        String code = request.getParameter("code");
        String email = authService.authorizeAndSaveToken(code);
        request.getSession().setAttribute("email", email);
        response.sendRedirect("/choose");
    }
}
