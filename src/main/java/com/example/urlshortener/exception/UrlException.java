package com.example.urlshortener.exception;

public class UrlException extends RuntimeException {
    
    private final String errorCode;
    
    public UrlException(String message) {
        super(message);
        this.errorCode = "URL_ERROR";
    }
    
    public UrlException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public static class InvalidUrlException extends UrlException {
        public InvalidUrlException(String message) {
            super(message, "INVALID_URL");
        }
    }
    
    public static class UrlNotFoundException extends UrlException {
        public UrlNotFoundException(String message) {
            super(message, "URL_NOT_FOUND");
        }
    }
    
    public static class BlockedUrlException extends UrlException {
        public BlockedUrlException(String message) {
            super(message, "BLOCKED_URL");
        }
    }
}