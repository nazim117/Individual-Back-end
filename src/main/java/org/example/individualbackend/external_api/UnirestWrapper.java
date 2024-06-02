package org.example.individualbackend.external_api;

import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UnirestWrapper {
    @Value("${rapidApi.key}")
    private String api_key;

    public HttpResponse<String> get(String url) throws UnirestException {
        return kong.unirest.Unirest.get(url)
                .header("x-rapidapi-key", api_key)
                .asString();
    }
}
