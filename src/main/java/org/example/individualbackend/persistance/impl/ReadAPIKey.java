package org.example.individualbackend.persistance.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadAPIKey {
    private static final String API_KEY_FILE_PATH = "apiKey.txt";
    public String readApiKeyFromFile() {
        try{
            return Files.readString(Paths.get(API_KEY_FILE_PATH));
        }catch (IOException e){
            return "";
        }
    }
}
