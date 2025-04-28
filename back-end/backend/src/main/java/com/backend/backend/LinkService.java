package com.backend.backend;

import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBException;

@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final UserService userService;

    @Autowired
    public LinkService(LinkRepository linkRepository, UserService userService) {
        this.linkRepository = linkRepository;
        this.userService = userService;
    }

    // Save a URL for future analysis
    public Link saveUrl(String url) throws JAXBException {
        User mostRecentUser = userService.getMostRecentUser();

        if (mostRecentUser == null) {
            throw new IllegalStateException("No logged-in user found.");
        }

        // Prevent duplicate URLs for the same user
        List<Link> existingLinks = linkRepository.findAllByUserId(mostRecentUser.getEmail());
        if (existingLinks.stream().anyMatch(link -> link.getUrl().equals(url))) {
            throw new IllegalArgumentException("This URL has already been saved.");
        }

        Link newLink = new Link();
        newLink.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE); // Generate unique ID
        newLink.setUrl(url);
        newLink.setUserId(mostRecentUser.getEmail());
        newLink.setResponseMessage("URL saved for future analysis");

        return linkRepository.save(newLink);
    }

    // List all URLs for the logged-in user
    public List<Link> listUrlsForUser() throws JAXBException {
        User mostRecentUser = userService.getMostRecentUser();

        if (mostRecentUser == null) {
            throw new IllegalStateException("No logged-in user found.");
        }

        return linkRepository.findAllByUserId(mostRecentUser.getEmail());
    }

    // Edit a saved URL
    public Link editUrl(Long id, String newUrl) throws JAXBException, IOException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID provided for the edit operation.");
        }
        if (newUrl == null || newUrl.isBlank()) {
            throw new IllegalArgumentException("New URL cannot be null or blank.");
        }
    
        // Retrieve the link and update the URL
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No link found with the provided ID."));
    
        link.setUrl(newUrl); // Overwrite the URL
        link.setResponseMessage("URL updated successfully.");
    
        // Save the updated link
        linkRepository.save(link);
    
        // Rescan the updated URL to scrape new details
        return rescanUrl(id);
    }

    // Remove a saved URL
    public void removeUrl(Long id) throws JAXBException {
        if (id == null) {
            throw new IllegalArgumentException("Invalid ID for delete operation.");
        }

        Optional<Link> link = linkRepository.findById(id);
        if (link.isEmpty()) {
            throw new IllegalArgumentException("Link with the provided ID does not exist.");
        }

        linkRepository.deleteById(id);
    }

    // Rescan a saved URL
    public Link rescanUrl(Long id) throws JAXBException, IOException {
        if (id == null) {
            throw new IllegalArgumentException("Invalid ID for rescan operation.");
        }
    
        Link existingLink = linkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No link found with the provided ID."));
    
        try {
            // Perform scraping logic
            URL websiteUrl = new URL(existingLink.getUrl());
            HttpsURLConnection connection = (HttpsURLConnection) websiteUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
    
            existingLink.setResponseCode(connection.getResponseCode());
            existingLink.setResponseMessage("Rescanned successfully");
            existingLink.setSslProtocol("TLS/SSL");
            existingLink.setSslCipher(connection.getCipherSuite());
    
            // Handle SSL certificate
            Certificate[] certificates = connection.getServerCertificates();
            if (certificates.length > 0 && certificates[0] instanceof X509Certificate cert) {
                existingLink.setCertificateInfo(String.format("Issuer: %s, Subject: %s, Expiration: %s",
                        cert.getIssuerX500Principal().getName(),
                        cert.getSubjectX500Principal().getName(),
                        cert.getNotAfter()));
            } else {
                existingLink.setCertificateInfo("No valid certificate found.");
            }
            connection.disconnect();
        } catch (Exception e) {
            existingLink.setResponseCode(500);
            existingLink.setResponseMessage("Failed to rescan the URL: " + e.getMessage());
            existingLink.setCertificateInfo("Failed to fetch SSL details");
        }
    
        return linkRepository.save(existingLink);
    }

    // Scrape and save URL details
    public Link scrapeAndSave(String url) throws JAXBException, IOException {
        User mostRecentUser = userService.getMostRecentUser();

        if (mostRecentUser == null) {
            throw new IllegalStateException("No logged-in user found.");
        }

        Link newLink = new Link();
        newLink.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE); // Generate unique ID
        newLink.setUserId(mostRecentUser.getEmail());
        newLink.setUrl(url);

        try {
            URL websiteUrl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) websiteUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            newLink.setResponseCode(connection.getResponseCode());
            newLink.setResponseMessage("Scraped successfully");
            newLink.setSslProtocol("TLS/SSL");
            newLink.setSslCipher(connection.getCipherSuite());

            // Handle SSL certificate
            Certificate[] certificates = connection.getServerCertificates();
            if (certificates.length > 0 && certificates[0] instanceof X509Certificate cert) {
                newLink.setCertificateInfo(String.format("Issuer: %s, Subject: %s, Expiration: %s",
                        cert.getIssuerX500Principal().getName(),
                        cert.getSubjectX500Principal().getName(),
                        cert.getNotAfter()));
            } else {
                newLink.setCertificateInfo("No valid certificate found.");
            }
            connection.disconnect();
        } catch (Exception e) {
            newLink.setResponseCode(500);
            newLink.setResponseMessage("Failed to scrape the URL: " + e.getMessage());
            newLink.setCertificateInfo("Failed to fetch SSL details");
        }

        return linkRepository.save(newLink);
    }
}