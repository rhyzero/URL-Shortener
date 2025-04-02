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
    
    public static class AliasAlreadyExistsException extends UrlException {
        public AliasAlreadyExistsException(String message) {
            super(message, "ALIAS_ALREADY_EXISTS");
        }
    }
    
    public static class InvalidAliasException extends UrlException {
        public InvalidAliasException(String message) {
            super(message, "INVALID_ALIAS");
        }
    }
    
    public static class UnauthorizedException extends UrlException {
        public UnauthorizedException(String message) {
            super(message, "UNAUTHORIZED");
        }
    }
    
    public static class UrlExpiredException extends UrlException {
        public UrlExpiredException(String message) {
            super(message, "URL_EXPIRED");
        }
    }
}