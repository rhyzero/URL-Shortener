package com.example.urlshortener.service;

import com.example.urlshortener.exception.UrlException;
import com.example.urlshortener.model.Url;

/**
 * Service for URL shortening and retrieval operations
 */
public interface UrlService {
    
    /**
     * Creates a shortened URL for the given original URL
     * If the URL has been shortened before, returns the existing entity
     * 
     * @param originalUrl the URL to be shortened
     * @return the Url entity containing both original and shortened URLs
     * @throws UrlException.InvalidUrlException if the URL is invalid
     * @throws UrlException.BlockedUrlException if the URL is blocked
     */
    Url shortenUrl(String originalUrl);
    
    /**
     * Retrieves the original URL for a given short URL
     * 
     * @param shortUrl the shortened URL to look up
     * @return the Url entity containing the original URL
     * @throws UrlException.UrlNotFoundException if the short URL is not found
     */
    Url getOriginalUrl(String shortUrl);
}