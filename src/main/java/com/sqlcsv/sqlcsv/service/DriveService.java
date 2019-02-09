package com.sqlcsv.sqlcsv.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.sqlcsv.sqlcsv.google.GoogleAuthorizationFlow;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DriveService implements IDriveService {
    public Map<String, String> getAllSpreadsheets() throws IOException, GeneralSecurityException {
        Drive userDrive = GoogleAuthorizationFlow.getDriveService();
        Drive.Files.List request = userDrive.files().list();
        FileList files = request.execute();

        return files.getFiles()
                .stream()
                .filter(s -> s.getMimeType().equals("application/vnd.google-apps.spreadsheet"))
                .collect(Collectors.toMap(File::getName, File::getId));
    }
}
