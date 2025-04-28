package com.backend.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class WebScraperController {

    @Autowired
    private WebScraper webScraper;

    @GetMapping("/scrape")
    public String scrapeUrl(@RequestParam String url) {
        return webScraper.scrapeUrl(url);
    }
}
