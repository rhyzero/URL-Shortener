package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        String originalUrl = request.get("url");
        if (originalUrl == null || originalUrl.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "MISSING_URL");
            error.put("message", "URL is required");
            return ResponseEntity.badRequest().body(error);
        }
        
        // The service will throw proper exceptions if the URL is invalid
        Url shortenedUrl = urlService.shortenUrl(originalUrl);
        
        Map<String, Object> response = new HashMap<>();
        response.put("originalUrl", shortenedUrl.getOriginalUrl());
        response.put("shortUrl", shortenedUrl.getShortUrl());
        response.put("createdBy", username);
        response.put("expiresAt", shortenedUrl.getExpiresAt());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Map<String, Object>> getOriginalUrl(@PathVariable String shortUrl) {
        if (shortUrl == null || shortUrl.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "MISSING_SHORT_URL");
            error.put("message", "Short URL is required");
            return ResponseEntity.badRequest().body(error);
        }
        
        // The service will throw a UrlNotFoundException if the URL is not found
        Url url = urlService.getOriginalUrl(shortUrl);
        
        Map<String, Object> response = new HashMap<>();
        response.put("originalUrl", url.getOriginalUrl());
        response.put("shortUrl", url.getShortUrl());
        response.put("createdAt", url.getCreatedAt());
        response.put("expiresAt", url.getExpiresAt());
        
        return ResponseEntity.ok(response);
    }
}