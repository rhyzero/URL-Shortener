package com.example.urlshortener.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator {

    /**
     * Validates if a given string is a valid URL
     * 
     * @param url the URL string to validate
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Validates and normalizes a URL
     * Ensures the URL has a protocol (defaults to http:// if missing)
     * 
     * @param url the URL string to validate and normalize
     * @return the normalized URL or null if invalid
     */
    public static String normalizeUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        
        if (!isValidUrl(url)) {
            return null;
        }
        
        return url;
    }
}