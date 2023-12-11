package com.url.shortener.controller;

import com.url.shortener.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RateLimitController {

    @Autowired
    private RateLimitService rateLimitService;

    @GetMapping("user/{id}")
    public ResponseEntity<?> getRateInfo(@PathVariable("id") String userId) {
        if (rateLimitService.allowedToMakeRequests(userId)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RATE LIMIT EXCEEDED");
        }
    }

}
