package com.sqlcsv.sqlcsv.controller;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.sqlcsv.sqlcsv.google.GoogleAuthorizationFlow;
import com.sqlcsv.sqlcsv.service.IDriveService;
import com.sqlcsv.sqlcsv.service.ISheetsService;
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
import java.util.List;
import java.util.Map;

@Controller
public class WebController {
    private IDriveService driveService;
    private ISheetsService sheetsService;

    @Autowired
    public WebController(IDriveService driveService, ISheetsService sheetsService) {
        this.sheetsService = sheetsService;
        this.driveService = driveService;
    }

    @GetMapping("/")
    public void redirectTo(HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth");
    }


    @GetMapping("/auth")
    public void getAuthPage(HttpServletResponse response) throws IOException, GeneralSecurityException {
        String url = GoogleAuthorizationFlow.getNewFlow().newAuthorizationUrl()
                .setRedirectUri("https://sql-csv.herokuapp.com/callback")
                .build();
        response.sendRedirect(url);
    }

    @GetMapping("/query")
    public String doGet(HttpServletRequest request, Model model) throws IOException, GeneralSecurityException {
        String spreadsheetId =  request.getParameter("spreadsheetId");
        List<String> sheetsNames = sheetsService.getSheetsNamesFromSpreadsheet(spreadsheetId);
        model.addAttribute("sheetNames", sheetsNames);
        return "home";
    }

    @GetMapping("/choose")
    public String getChoosePage(Model model) throws IOException, GeneralSecurityException {
        Map<String, String> spreadsheets = driveService.getAllSpreadsheets();
        model.addAttribute("spreadsheets", spreadsheets);
        return "choosePage";
    }

    @PostMapping("/choose")
    public void redirectToQueryPage(HttpServletResponse response, @RequestParam("spreadsheetId") String spreadsheetId) throws IOException {
        response.sendRedirect("/query?spreadsheetId=" + spreadsheetId);
    }

    @GetMapping("/callback")
    public void getToken(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
        String code = request.getParameter("code");
        authorizeAndSaveToken(code);
        response.sendRedirect("https://sql-csv.herokuapp.com/choose");
    }

    private void authorizeAndSaveToken(String code) throws IOException, GeneralSecurityException {
        GoogleAuthorizationCodeFlow flow = GoogleAuthorizationFlow.getNewFlow();
        GoogleAuthorizationCodeTokenRequest query = flow
                .newTokenRequest(code)
                .setRedirectUri("https://sql-csv.herokuapp.com/callback")
                .setClientAuthentication(flow.getClientAuthentication())
                .setCode(code)
                .set("response-type", "code")
                .setGrantType("authorization_code");

        TokenResponse tokenResponse = query.execute();
        flow.createAndStoreCredential(tokenResponse,"user");
    }
}
