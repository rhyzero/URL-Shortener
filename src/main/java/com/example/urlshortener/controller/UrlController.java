package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String, String> request) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        String originalUrl = request.get("url");
        if (originalUrl == null || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
        }
        
        try {
            Url shortenedUrl = urlService.shortenUrl(originalUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("originalUrl", shortenedUrl.getOriginalUrl());
            response.put("shortUrl", shortenedUrl.getShortUrl());
            response.put("createdBy", username);
            response.put("expiresAt", shortenedUrl.getExpiresAt());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create short URL"));
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable String shortUrl) {
        if (shortUrl == null || shortUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Short URL is required"));
        }
        
        return urlService.getOriginalUrl(shortUrl)
                .map(url -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("originalUrl", url.getOriginalUrl());
                    response.put("shortUrl", url.getShortUrl());
                    response.put("createdAt", url.getCreatedAt());
                    response.put("expiresAt", url.getExpiresAt());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}