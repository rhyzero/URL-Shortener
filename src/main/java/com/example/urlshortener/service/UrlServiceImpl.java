package com.example.urlshortener.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.UrlValidator;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url shortenUrl(String originalUrl) {
        String normalizedUrl = UrlValidator.normalizeUrl(originalUrl);
        if (normalizedUrl == null) {
            throw new IllegalArgumentException("Invalid URL format");
        }
        
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(normalizedUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        String shortUrl = generateShortUrl(normalizedUrl);
        
        while (urlRepository.existsByShortUrl(shortUrl)) {
            shortUrl = shortUrl + generateRandomChar();
        }

        Url url = new Url();
        url.setOriginalUrl(normalizedUrl);
        url.setShortUrl(shortUrl);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(LocalDateTime.now().plusYears(1));
        
        return urlRepository.save(url);
    }

    @Override
    public Optional<Url> getOriginalUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    /**
     * Generates a short URL using MD5 hashing and Base64 encoding
     * @param originalUrl the original URL to shorten
     * @return a short URL code
     */
    private String generateShortUrl(String originalUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            
            String base64Encoded = Base64.getUrlEncoder().encodeToString(hash);
            
            return base64Encoded.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(originalUrl.hashCode()).replace("-", "").substring(0, 8);
        }
    }
    
    /**
     * Generates a random character to append to a short URL to ensure uniqueness
     * @return a random character
     */
    private char generateRandomChar() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return chars.charAt((int) (Math.random() * chars.length()));
    }
}