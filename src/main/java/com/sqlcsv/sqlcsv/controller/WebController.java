package com.sqlcsv.sqlcsv.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.sqlcsv.sqlcsv.google.GoogleAuthorizationFlow;
import com.sqlcsv.sqlcsv.service.IDriveService;
import com.sqlcsv.sqlcsv.service.IQueryService;
import com.sqlcsv.sqlcsv.service.ISheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {
    private IDriveService driveService;
    private ISheetsService sheetsService;
    private IQueryService queryService;

    @Autowired
    public WebController(IDriveService driveService, ISheetsService sheetsService, IQueryService queryService) {
        this.sheetsService = sheetsService;
        this.driveService = driveService;
        this.queryService = queryService;
    }

    @GetMapping("/")
    public void redirectTo(HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth");
    }


    @GetMapping("/auth")
    public void getAuthPage(HttpServletResponse response) throws IOException, GeneralSecurityException {
        String url = GoogleAuthorizationFlow.getNewFlow().newAuthorizationUrl()
                .setRedirectUri("http://localhost:8080/callback")
                .build();
        response.sendRedirect(url);
    }

    @GetMapping("/query")
    public String doGet(HttpServletRequest request, Model model) throws IOException, GeneralSecurityException {
        String spreadsheetId =  request.getParameter("spreadsheetId");
        String userId = request.getSession().getAttribute("email").toString();
        List<String> sheetsNames = sheetsService.getSheetsNamesFromSpreadsheet(spreadsheetId, userId);
        model.addAttribute("sheetNames", sheetsNames);
        return "home";
    }

    @PostMapping("/query")
    public String doPost(@RequestParam("query") String query) {
        System.out.println(query);
        return "home";
    }

    @GetMapping("/choose")
    public String getChoosePage(Model model, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String userId = request.getSession().getAttribute("email").toString();
        Map<String, String> spreadsheets = driveService.getAllSpreadsheets(userId);
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
        String email = authorizeAndSaveToken(code);
        HttpSession session = request.getSession();
        session.setAttribute("email", email);
        response.sendRedirect("/choose");
    }

    private String authorizeAndSaveToken(String code) throws IOException, GeneralSecurityException {
        GoogleAuthorizationCodeFlow flow = GoogleAuthorizationFlow.getNewFlow();

        GoogleAuthorizationCodeTokenRequest query = flow
                .newTokenRequest(code)
                .setRedirectUri("http://localhost:8080/callback")
                .setClientAuthentication(flow.getClientAuthentication())
                .setCode(code)
                .set("response-type", "code")
                .setGrantType("authorization_code");

        GoogleTokenResponse tokenResponse = query.execute();
        String userId = getUserEmail(tokenResponse);
        flow.createAndStoreCredential(tokenResponse, userId);
        return userId;
    }

    private String getUserEmail(GoogleTokenResponse tokenResponse) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + tokenResponse.getAccessToken());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(inputStream);
        return json.findValue("email").toString().replaceAll("\"", "");
    }
}
