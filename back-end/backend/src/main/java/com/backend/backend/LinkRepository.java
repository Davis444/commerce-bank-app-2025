package com.backend.backend;

import org.springframework.stereotype.Repository;
import jakarta.xml.bind.JAXBException;
import java.util.List;
import java.util.Optional;

@Repository
public class LinkRepository {

    private final XMLUtils xmlUtils;

    public LinkRepository(XMLUtils xmlUtils) {
        this.xmlUtils = xmlUtils;
    }

    /**
     * Save a Link to the repository. If the Link has an ID, update the existing entry; otherwise, assign a new ID.
     */
    public Link save(Link link) throws JAXBException {
        List<Link> links = xmlUtils.readLinksFromXML();

        if (link.getId() != null) {
            // Update the existing link by ID
            for (int i = 0; i < links.size(); i++) {
                if (links.get(i).getId() != null && links.get(i).getId().equals(link.getId())) {
                    links.set(i, link);
                    xmlUtils.writeLinksToXML(links);
                    return link;
                }
            }
        }

        // Assign a unique ID to new links
        Long newId = links.stream()
                .map(Link::getId)
                .filter(id -> id != null)
                .max(Long::compareTo)
                .orElse(0L) + 1;
        link.setId(newId);
        links.add(link);

        // Write updated links list to XML
        xmlUtils.writeLinksToXML(links);
        return link;
    }

    /**
     * Retrieve all Links associated with a specific user ID.
     */
    public List<Link> findAllByUserId(String userId) throws JAXBException {
        List<Link> links = xmlUtils.readLinksFromXML();

        // Filter links by user ID
        return links.stream()
                .filter(link -> userId != null && userId.equals(link.getUserId()))
                .toList();
    }

    /**
     * Find a specific Link by its ID.
     */
    public Optional<Link> findById(Long id) throws JAXBException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }

        List<Link> links = xmlUtils.readLinksFromXML();
        return links.stream()
                .filter(link -> link.getId() != null && link.getId().equals(id))
                .findFirst();
    }

    /**
     * Delete a specific Link by its ID.
     */
    public void deleteById(Long id) throws JAXBException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }

        List<Link> links = xmlUtils.readLinksFromXML();

        // Remove the link with the given ID
        links.removeIf(link -> link.getId() != null && link.getId().equals(id));

        // Write updated links list to XML
        xmlUtils.writeLinksToXML(links);
    }

    /**
     * Validate and sanitize links before writing to XML (Optional Helper Method).
     */
    public void cleanLinksBeforeWrite(List<Link> links) throws JAXBException {
        // Ensure no links with null IDs are written to XML
        List<Link> validLinks = links.stream()
                .filter(link -> link.getId() != null)
                .toList();

        xmlUtils.writeLinksToXML(validLinks);
    }
}