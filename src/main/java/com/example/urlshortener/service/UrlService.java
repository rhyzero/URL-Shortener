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
     * Creates a shortened URL with a custom alias
     * 
     * @param originalUrl the URL to be shortened
     * @param customAlias the custom alias to use for the short URL
     * @return the Url entity containing both original and shortened URLs
     * @throws UrlException.InvalidUrlException if the URL is invalid
     * @throws UrlException.BlockedUrlException if the URL is blocked
     * @throws UrlException.AliasAlreadyExistsException if the custom alias is already in use
     */
    Url shortenUrl(String originalUrl, String customAlias);
    
    /**
     * Retrieves the original URL for a given short URL
     * 
     * @param shortUrl the shortened URL to look up
     * @return the Url entity containing the original URL
     * @throws UrlException.UrlNotFoundException if the short URL is not found
     */
    Url getOriginalUrl(String shortUrl);
    
    /**
     * Deletes a URL by its short URL
     * Only the creator or an admin can delete the URL
     * 
     * @param shortUrl the shortened URL to delete
     * @param username the username of the requester
     * @throws UrlException.UrlNotFoundException if the short URL is not found
     * @throws UrlException.UnauthorizedException if the user is not authorized to delete this URL
     */
    void deleteUrl(String shortUrl, String username);
}