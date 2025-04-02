package com.example.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UrlException.InvalidUrlException.class)
    public ResponseEntity<Object> handleInvalidUrlException(UrlException.InvalidUrlException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UrlException.UrlNotFoundException.class)
    public ResponseEntity<Object> handleUrlNotFoundException(UrlException.UrlNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UrlException.BlockedUrlException.class)
    public ResponseEntity<Object> handleBlockedUrlException(UrlException.BlockedUrlException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "INTERNAL_SERVER_ERROR");
        body.put("message", "An unexpected error occurred");
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}