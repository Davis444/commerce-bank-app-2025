package com.backend.backend;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "links")
public class LinkListWrapper {

    private List<Link> links;

    @XmlElement(name = "link")
    public List<Link> getLinks() {
        if (links == null) {
            links = new ArrayList<>(); // Initialize to prevent null issues
        }
        return links;
    }

    public void setLinks(List<Link> links) {
        if (links == null) {
            throw new IllegalArgumentException("Links cannot be null.");
        }
        this.links = links;
    }
}