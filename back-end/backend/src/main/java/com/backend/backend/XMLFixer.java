package com.backend.backend;

import java.util.List;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

public class XMLFixer {

    public static void main(String[] args) throws JAXBException {
        File xmlFile = new File("path/to/your/links.xml");
        JAXBContext context = JAXBContext.newInstance(LinkListWrapper.class);

        // Deserialize XML to Java objects
        Unmarshaller unmarshaller = context.createUnmarshaller();
        LinkListWrapper wrapper = (LinkListWrapper) unmarshaller.unmarshal(xmlFile);

        // Validate and assign missing IDs
        List<Link> links = wrapper.getLinks();
        long nextId = links.stream()
                .map(Link::getId)
                .filter(id -> id != null)
                .max(Long::compareTo)
                .orElse(0L) + 1;

        for (Link link : links) {
            if (link.getId() == null) {
                link.setId(nextId++);
            }
        }

        // Serialize the updated Java objects back to XML
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, xmlFile);

        System.out.println("Missing IDs fixed and XML updated successfully!");
    }
}