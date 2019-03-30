package com.sqlcsv.sqlcsv.service.googleservice;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.sqlcsv.sqlcsv.google.GoogleAuthorizationFlow;
import com.sqlcsv.sqlcsv.enums.ServicesEnum;
import com.sqlcsv.sqlcsv.interfaces.IDriveService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DriveService implements IDriveService {
    private static final String SPREADSHEET = "application/vnd.google-apps.spreadsheet";
    public Map<String, String> getAllSpreadsheets(String userId) throws IOException, GeneralSecurityException {
        Drive userDrive = (Drive) GoogleAuthorizationFlow.getService(ServicesEnum.DRIVE, userId);
        FileList files = Objects.requireNonNull(userDrive).files().list().execute();

        return files.getFiles()
                .stream()
                .filter(s -> s.getMimeType().equals(SPREADSHEET))
                .collect(Collectors.toMap(File::getName, File::getId));
    }
}
