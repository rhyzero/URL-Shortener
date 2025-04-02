package com.example.urlshortener.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createShortUrl(@RequestBody Map<String, String> request) {
        try {
            String originalUrl = request.get("url");
            if (originalUrl == null || originalUrl.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "URL is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Url shortenedUrl = urlService.shortenUrl(originalUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("originalUrl", shortenedUrl.getOriginalUrl());
            response.put("shortUrl", shortenedUrl.getShortUrl());
            response.put("createdAt", shortenedUrl.getCreatedAt());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create short URL: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Map<String, Object>> getOriginalUrl(@PathVariable String shortUrl) {
        try {
            Url url = urlService.getOriginalUrl(shortUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("originalUrl", url.getOriginalUrl());
            response.put("shortUrl", url.getShortUrl());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get URL: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}