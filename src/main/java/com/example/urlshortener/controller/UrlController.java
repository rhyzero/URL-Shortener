package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        String originalUrl = request.get("url");
        if (originalUrl == null || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("URL is required");
        }
        
        Url shortenedUrl = urlService.shortenUrl(originalUrl);
        return ResponseEntity.ok(Map.of(
            "originalUrl", shortenedUrl.getOriginalUrl(),
            "shortUrl", shortenedUrl.getShortUrl()
        ));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable String shortUrl) {
        return urlService.getOriginalUrl(shortUrl)
                .map(url -> ResponseEntity.ok(Map.of(
                    "originalUrl", url.getOriginalUrl(),
                    "shortUrl", url.getShortUrl()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}