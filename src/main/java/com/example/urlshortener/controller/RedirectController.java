package com.example.urlshortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import com.example.urlshortener.exception.UrlException;
import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;

@Controller
public class RedirectController {

    private final UrlService urlService;

    @Autowired
    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirectToOriginalUrl(@PathVariable String shortUrl) {
        try {
            Url url = urlService.getOriginalUrl(shortUrl);
            
            try {
                urlService.incrementClickCount(shortUrl);
            } catch (Exception e) {
                System.err.println("Error incrementing click count: " + e.getMessage());
            }
            
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(url.getOriginalUrl());
            return redirectView;
        } catch (UrlException.UrlNotFoundException e) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/not-found");
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return redirectView;
        } catch (UrlException.UrlExpiredException e) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/expired");
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return redirectView;
        }
    }
    
    @GetMapping("/not-found")
    public String notFoundPage() {
        return "not-found";
    }
    
    @GetMapping("/expired")
    public String expiredPage() {
        return "expired";
    }
}