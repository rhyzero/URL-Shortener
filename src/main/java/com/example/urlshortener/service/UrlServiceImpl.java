package com.example.urlshortener.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.urlshortener.exception.UrlException;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.UrlValidator;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    
    private static final Pattern ALIAS_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]{1,20}$");

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
        url.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        
        return urlRepository.save(url);
    }
    
    @Override
    public Url shortenUrl(String originalUrl, String customAlias) {
        String normalizedUrl = UrlValidator.normalizeUrl(originalUrl);
        if (normalizedUrl == null) {
            throw new UrlException.InvalidUrlException("Invalid URL format: " + originalUrl);
        }
        
        if (!ALIAS_PATTERN.matcher(customAlias).matches()) {
            throw new UrlException.InvalidAliasException(
                "Custom alias must be 1-20 characters and can only contain letters, numbers, hyphens, and underscores");
        }
        
        if (urlRepository.existsByShortUrl(customAlias)) {
            throw new UrlException.AliasAlreadyExistsException(
                "The custom alias '" + customAlias + "' is already in use. Please choose another one.");
        }
        
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(normalizedUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        Url url = new Url();
        url.setOriginalUrl(normalizedUrl);
        url.setShortUrl(customAlias);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(LocalDateTime.now().plusYears(1));
        url.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        
        return urlRepository.save(url);
    }

    @Override
    public Url getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlException.UrlNotFoundException("Short URL not found: " + shortUrl));
        
        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlException.UrlExpiredException("URL has expired: " + shortUrl);
        }
        
        return url;
    }
    
    @Override
    @Transactional
    public void deleteUrl(String shortUrl, String username) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlException.UrlNotFoundException("Short URL not found: " + shortUrl));
        
        boolean isAdmin = hasRole("ROLE_ADMIN");
        boolean isCreator = username.equals(url.getCreatedBy());
        
        if (!isAdmin && !isCreator) {
            throw new UrlException.UnauthorizedException(
                "You are not authorized to delete this URL. Only the creator or an admin can delete it.");
        }
        
        urlRepository.delete(url);
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
    
    /**
     * Checks if the current user has a specific role
     * @param role the role to check
     * @return true if the user has the role, false otherwise
     */
    private boolean hasRole(String role) {
        Collection<? extends GrantedAuthority> authorities = 
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(role));
    }
}