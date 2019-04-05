package com.sqlcsv.sqlcsv.service.googleservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.sqlcsv.sqlcsv.google.GoogleAuthorizationFlow;
import com.sqlcsv.sqlcsv.interfaces.IAuthService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;

@Service
public class AuthService implements IAuthService {

    @Override
    public String createNewAuthorizationUrl() throws IOException, GeneralSecurityException {
        return GoogleAuthorizationFlow.getFlow().newAuthorizationUrl()
                .setRedirectUri("http://localhost:8080/callback")
                .build();
    }

    @Override
    public String authorizeAndSaveToken(String code) throws IOException, GeneralSecurityException {
        GoogleAuthorizationCodeFlow flow = GoogleAuthorizationFlow.getFlow();
        GoogleTokenResponse tokenResponse = flow
                .newTokenRequest(code)
                .setRedirectUri("http://localhost:8080/callback")
                .setClientAuthentication(flow.getClientAuthentication())
                .setCode(code)
                .set("response-type", "code")
                .setGrantType("authorization_code")
                .execute();
        String userId = getUserEmail(tokenResponse);
        flow.createAndStoreCredential(tokenResponse, userId);
        return userId;
    }

    private String getUserEmail(GoogleTokenResponse tokenResponse) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + tokenResponse.getAccessToken()).openConnection();
        connection.setRequestMethod("GET");
        JsonNode json = new ObjectMapper().readTree(connection.getInputStream());
        return json.findValue("email").toString().replaceAll("\"", "");
    }
}
