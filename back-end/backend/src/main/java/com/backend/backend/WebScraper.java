package com.backend.backend;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebScraper {

    private final RestTemplate restTemplate;

    public WebScraper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String scrapeUrl(String url) {
        try {
            String html = restTemplate.getForObject(url, String.class);
            return Jsoup.parse(html).text();
        } catch (Exception e) {
            return "Error fetching URL: " + e.getMessage();
        }
    }
}
