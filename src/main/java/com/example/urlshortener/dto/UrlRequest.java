package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UrlRequest {
    
    @NotBlank(message = "URL cannot be empty")
    @Size(max = 2048, message = "URL cannot exceed 2048 characters")
    @Pattern(
        regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,})([/\\w.-]*)*/?$",
        message = "Invalid URL format"
    )
    private String url;
    
    @Size(max = 20, message = "Custom alias cannot exceed 20 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9-_]*$",
        message = "Custom alias can only contain letters, numbers, hyphens, and underscores"
    )
    private String customAlias;
    
    public UrlRequest() {
    }
    
    public UrlRequest(String url) {
        this.url = url;
    }
    
    public UrlRequest(String url, String customAlias) {
        this.url = url;
        this.customAlias = customAlias;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getCustomAlias() {
        return customAlias;
    }
    
    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }
}