package com.example.urlshortener.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;

@RestController
@RequestMapping("/api/stats")
public class UrlStatsController {

    private final UrlService urlService;

    @Autowired
    public UrlStatsController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Map<String, Object>> getUrlStats(@PathVariable String shortUrl) {
        Url url = urlService.getOriginalUrl(shortUrl);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("shortUrl", url.getShortUrl());
        stats.put("originalUrl", url.getOriginalUrl());
        stats.put("createdAt", url.getCreatedAt());
        stats.put("expiresAt", url.getExpiresAt());
        stats.put("clickCount", url.getClickCount());
        
        return ResponseEntity.ok(stats);
    }
}