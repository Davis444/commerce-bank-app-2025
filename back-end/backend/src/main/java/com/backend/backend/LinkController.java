package com.backend.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import jakarta.xml.bind.JAXBException;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/scrape")
    public ResponseEntity<Link> scrapeUrl(@RequestParam String url) throws JAXBException, IOException {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or blank.");
        }
        Link scrapedLink = linkService.scrapeAndSave(url);
        return ResponseEntity.ok(scrapedLink);
    }

    @PostMapping("/rescan")
    public ResponseEntity<Link> rescanUrl(@RequestParam Long id) throws JAXBException, IOException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("A valid ID is required for rescan.");
        }
        Link rescannedLink = linkService.rescanUrl(id);
        return ResponseEntity.ok(rescannedLink);
    }

    @PutMapping("/edit")
    public ResponseEntity<Link> editUrl(@RequestParam Long id, @RequestParam String newUrl) throws JAXBException, IOException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("A valid ID is required for edit.");
        }
        if (newUrl == null || newUrl.isBlank()) {
            throw new IllegalArgumentException("New URL cannot be null or blank.");
        }
        Link updatedLink = linkService.editUrl(id, newUrl);
        return ResponseEntity.ok(updatedLink);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeUrl(@RequestParam Long id) throws JAXBException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("A valid ID is required for delete.");
        }
        linkService.removeUrl(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    public ResponseEntity<Link> saveUrl(@RequestParam String url) throws JAXBException {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or blank.");
        }
        Link savedLink = linkService.saveUrl(url);
        return ResponseEntity.ok(savedLink);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Link>> listUrlsForUser() throws JAXBException {
        List<Link> links = linkService.listUrlsForUser();
        return ResponseEntity.ok(links);
    }
}