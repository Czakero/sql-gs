package com.sqlcsv.sqlcsv.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface IDriveService {
    Map<String, String> getAllSpreadsheets() throws IOException, GeneralSecurityException;
}
