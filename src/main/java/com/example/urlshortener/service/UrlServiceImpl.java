package com.example.urlshortener.service;

import com.example.urlshortener.exception.UrlException;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url shortenUrl(String originalUrl) {
        // Validate and normalize URL
        String normalizedUrl = UrlValidator.normalizeUrl(originalUrl);
        if (normalizedUrl == null) {
            throw new UrlException.InvalidUrlException("Invalid URL format: " + originalUrl);
        }
        
        // Check if URL already exists in the database
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(normalizedUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        // Generate short URL
        String shortUrl = generateShortUrl(normalizedUrl);
        
        // Ensure the short URL is unique
        while (urlRepository.existsByShortUrl(shortUrl)) {
            shortUrl = shortUrl + generateRandomChar();
        }

        // Create and save the new URL entity
        Url url = new Url();
        url.setOriginalUrl(normalizedUrl);
        url.setShortUrl(shortUrl);
        url.setCreatedAt(LocalDateTime.now());
        // Setting expiration to 1 year from now as default
        url.setExpiresAt(LocalDateTime.now().plusYears(1));
        
        return urlRepository.save(url);
    }

    @Override
    public Url getOriginalUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlException.UrlNotFoundException("Short URL not found: " + shortUrl));
    }

    /**
     * Generates a short URL using MD5 hashing and Base64 encoding
     * @param originalUrl the original URL to shorten
     * @return a short URL code
     */
    private String generateShortUrl(String originalUrl) {
        try {
            // Create MD5 hash of the original URL
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            
            // Encode the hash using Base64 to make it URL-safe
            String base64Encoded = Base64.getUrlEncoder().encodeToString(hash);
            
            // Take first 8 characters as the short URL
            return base64Encoded.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            // Fallback to a simple hash if MD5 is not available
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