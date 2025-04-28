package com.backend.backend;

import org.springframework.stereotype.Service;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class XMLUtils {

    // Paths for XML files
    private final String usersXmlPath = "C:\\Users\\cgdav\\OneDrive\\Desktop\\Main\\College\\Central Mo\\UCM Senior Year\\First Semester\\Software Engineering\\commerce-bank-app-2025\\back-end\\backend\\src\\main\\resources\\users.xml";
    private final String linksXmlPath = "C:\\Users\\cgdav\\OneDrive\\Desktop\\Main\\College\\Central Mo\\UCM Senior Year\\First Semester\\Software Engineering\\commerce-bank-app-2025\\back-end\\backend\\src\\main\\resources\\links.xml";

    // Method to read all users from the XML file
    public List<User> readUsersFromXML() throws JAXBException {
        File usersXmlFile = new File(usersXmlPath);
        if (!usersXmlFile.exists()) {
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }

        JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        UserListWrapper wrapper = (UserListWrapper) unmarshaller.unmarshal(usersXmlFile);
        return wrapper.getUsers();
    }

    // Method to write users to the XML file
    public void writeUsersToXML(List<User> users) throws JAXBException {
        File usersXmlFile = new File(usersXmlPath);
        JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        UserListWrapper wrapper = new UserListWrapper();
        wrapper.setUsers(users);
        marshaller.marshal(wrapper, usersXmlFile);
    }

    // Method to read all links from the XML file
    public List<Link> readLinksFromXML() throws JAXBException {
        File linksXmlFile = new File(linksXmlPath);
        if (!linksXmlFile.exists()) {
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }

        JAXBContext context = JAXBContext.newInstance(LinkListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        LinkListWrapper wrapper = (LinkListWrapper) unmarshaller.unmarshal(linksXmlFile);
        return wrapper.getLinks();
    }

    // Method to write links to the XML file
    public void writeLinksToXML(List<Link> links) throws JAXBException {
        File linksXmlFile = new File(linksXmlPath);
        JAXBContext context = JAXBContext.newInstance(LinkListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LinkListWrapper wrapper = new LinkListWrapper();
        wrapper.setLinks(links);
        marshaller.marshal(wrapper, linksXmlFile);
    }
}