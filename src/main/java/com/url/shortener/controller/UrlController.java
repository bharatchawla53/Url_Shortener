package com.url.shortener.controller;

import com.url.shortener.model.UrlShortenerResponse;
import com.url.shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class UrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/welcome")
    public String welcome() {
        return "WELCOME TO URL SHORTENER APP!";
    }

    @PostMapping("/url")
    public ResponseEntity<?> urlShortner(@RequestParam(name = "longUrl") String longUrl) {

        UrlShortenerResponse response = urlShortenerService.convertUrl(longUrl);

        return ResponseEntity.ok().body(response.getShortUrl());
    }

    @GetMapping("/url/history")
    public ResponseEntity<?> getUrls() {
        List<UrlShortenerResponse> responseList = urlShortenerService.getUrlHistory();

        return ResponseEntity.ok().body(responseList);
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> redirect(@RequestParam(name = "shortUrl") String shortUrl) {
        String longUrl = urlShortenerService.getLongUrl(shortUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
