package org.example.individualbackend.persistance.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReadAPIKey {
    @Value("${token}")
    private String apiKey;


}
