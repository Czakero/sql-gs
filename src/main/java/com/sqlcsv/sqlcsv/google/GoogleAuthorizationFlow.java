package com.sqlcsv.sqlcsv.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Component
@Getter
public class GoogleAuthorizationFlow {

    private static final String APPLICATION_NAME = "sqlyourcsv";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File("src/main/resources");
    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_READONLY, SheetsScopes.SPREADSHEETS_READONLY);
    private static GoogleAuthorizationCodeFlow flow;

    public static GoogleAuthorizationCodeFlow getNewFlow() throws IOException, GeneralSecurityException {
        return flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
                getClientSecrets(), SCOPES).setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                .setAccessType("offline").setApprovalPrompt("force").build();
    }

    private static GoogleClientSecrets getClientSecrets() throws IOException {
        java.io.File clientSecretFile = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
        InputStream clientSecretInputStream = new FileInputStream(clientSecretFile);
        return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(clientSecretInputStream));
    }
    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, flow.loadCredential("user"))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, flow.loadCredential("user"))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}