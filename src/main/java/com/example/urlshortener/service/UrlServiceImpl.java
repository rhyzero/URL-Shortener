package com.example.urlshortener.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url shortenUrl(String originalUrl) {
        // Simple placeholder implementation - will be enhanced in later commits
        String shortUrl = generateShortUrl();
        Url url = new Url(originalUrl, shortUrl);
        return urlRepository.save(url);
    }

    @Override
    public Optional<Url> getOriginalUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    /**
     * Generates a simple random short URL
     * This is a placeholder implementation that will be improved in later commits
     */
    private String generateShortUrl() {
        // Generate a random string for the short URL
        // This is a naive implementation and will be improved in later commits
        return UUID.randomUUID().toString().substring(0, 8);
    }
}