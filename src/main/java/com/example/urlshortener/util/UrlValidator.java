package com.example.urlshortener.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class UrlValidator {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    
    private static final Set<String> VALID_PROTOCOLS = new HashSet<>(Arrays.asList(
            "http", "https", "ftp"
    ));
    
    private static final Set<String> BLOCKED_DOMAINS = new HashSet<>(Arrays.asList(
            "example.com", "malicious-site.com", "spam.com"
    ));

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
        
        // Check against regex pattern first for quick validation
        if (!URL_PATTERN.matcher(url).matches()) {
            return false;
        }
        
        try {
            // Try to create a URL object to validate the URL format
            URL urlObj = new URL(url);
            
            // Check protocol
            String protocol = urlObj.getProtocol().toLowerCase();
            if (!VALID_PROTOCOLS.contains(protocol)) {
                return false;
            }
            
            // Check for blocked domains
            String host = urlObj.getHost().toLowerCase();
            for (String blockedDomain : BLOCKED_DOMAINS) {
                if (host.endsWith(blockedDomain)) {
                    return false;
                }
            }
            
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
        
        // Add http:// protocol if missing
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://")) {
            url = "http://" + url;
        }
        
        if (!isValidUrl(url)) {
            return null;
        }
        
        try {
            URL urlObj = new URL(url);
            String protocol = urlObj.getProtocol();
            String host = urlObj.getHost();
            int port = urlObj.getPort();
            String path = urlObj.getPath();
            String query = urlObj.getQuery();
            String fragment = urlObj.getRef();
            
            StringBuilder normalizedUrl = new StringBuilder();
            normalizedUrl.append(protocol).append("://").append(host);
            
            if (port != -1) {
                normalizedUrl.append(":").append(port);
            }
            
            normalizedUrl.append(path);
            
            if (query != null && !query.isEmpty()) {
                normalizedUrl.append("?").append(query);
            }
            
            if (fragment != null && !fragment.isEmpty()) {
                normalizedUrl.append("#").append(fragment);
            }
            
            return normalizedUrl.toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}