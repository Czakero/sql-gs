package com.sqlcsv.sqlcsv.interfaces;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface IDriveService {
    Map<String, String> getAllSpreadsheets(String userId) throws IOException, GeneralSecurityException;
}
