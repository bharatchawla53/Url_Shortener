package com.url.shortener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.url.shortener.model.UrlShortenerResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class UrlShortenerService {

    private static final String FILEPATH = "src/main/resources/history.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UrlShortenerResponse convertUrl(String longUrl) {
        // convert long url to a short url
        String shortUrl = shortUrlConverter(longUrl);

        // create url shortener object
        UrlShortenerResponse response = UrlShortenerResponse.builder()
                .id(generateId())
                .shortUrl(shortUrl)
                .longUrl(longUrl)
                .build();

        // save this new response to a file to keep track of history
        saveResponse(response);

        return response;
    }

    public List<UrlShortenerResponse> getUrlHistory() {
        return readExistingData();
    }

    public String getLongUrl(String shortUrl) {
        List<UrlShortenerResponse> history = readExistingData();

        return history.stream()
                .filter(h -> h.getShortUrl().equals(shortUrl))
                .findFirst()
                .map(UrlShortenerResponse::getLongUrl)
                .orElse(null);
    }

    private String shortUrlConverter(String longUrl) {
        String shortUrl = "https://short-url.com/";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));

            String encoded = Base64.getUrlEncoder().encodeToString(hash).substring(0, 5);
            shortUrl += encoded;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return shortUrl;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private void saveResponse(UrlShortenerResponse response) {

        List<UrlShortenerResponse> existingData = readExistingData();

        if (existingData == null) {
            existingData = new ArrayList<>();
        }

        existingData.add(response);

        try {
            objectMapper.writeValue(new File(FILEPATH), existingData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<UrlShortenerResponse> readExistingData() {

        try {
            return objectMapper.readValue(new File(FILEPATH), new TypeReference<List<UrlShortenerResponse>>() {});
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
