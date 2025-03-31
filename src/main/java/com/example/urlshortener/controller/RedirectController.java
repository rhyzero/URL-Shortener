package com.example.urlshortener.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

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
        Optional<Url> urlOptional = urlService.getOriginalUrl(shortUrl);
        
        if (urlOptional.isPresent()) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(urlOptional.get().getOriginalUrl());
            return redirectView;
        } else {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/not-found");
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return redirectView;
        }
    }
    
    @GetMapping("/not-found")
    public String notFoundPage() {
        return "not-found";
    }
}