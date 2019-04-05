package com.sqlcsv.sqlcsv.interfaces;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IAuthService {
    String createNewAuthorizationUrl() throws IOException, GeneralSecurityException;
    String authorizeAndSaveToken(String code) throws IOException, GeneralSecurityException;
}
