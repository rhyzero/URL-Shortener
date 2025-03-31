package com.example.urlshortener.service;

import java.util.Optional;

import com.example.urlshortener.model.Url;

public interface UrlService {
    
    /**
     * Creates a shortened URL for the given original URL
     * 
     * @param originalUrl the URL to be shortened
     * @return the Url entity containing both original and shortened URLs
     */
    Url shortenUrl(String originalUrl);
    
    /**
     * Retrieves the original URL for a given short URL
     * 
     * @param shortUrl the shortened URL to look up
     * @return Optional containing the Url entity if found, empty otherwise
     */
    Optional<Url> getOriginalUrl(String shortUrl);
}
