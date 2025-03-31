package com.example.urlshortener.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.urlshortener.model.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    
    /**
     * Find a URL entity by its short URL
     * @param shortUrl the shortened URL string
     * @return an Optional containing the URL entity if found
     */
    Optional<Url> findByShortUrl(String shortUrl);
    
    /**
     * Check if a short URL already exists
     * @param shortUrl the shortened URL string
     * @return true if the short URL exists, false otherwise
     */
    boolean existsByShortUrl(String shortUrl);
}