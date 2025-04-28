package com.backend.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBException;

import java.util.List;

@Service
public class UserService {

    private final XMLUtils xmlUtils;

    @Autowired
    public UserService(XMLUtils xmlUtils) {
        this.xmlUtils = xmlUtils; // Inject XMLUtils
    }

    // Add User logic
    public void addUser(User newUser) throws JAXBException {
        List<User> users = xmlUtils.readUsersFromXML();
        users.add(newUser);
        xmlUtils.writeUsersToXML(users);
    }

    public User getMostRecentUser() throws JAXBException {
        List<User> users = xmlUtils.readUsersFromXML(); // Read all users from users.xml
        if (!users.isEmpty()) {
            return users.get(users.size() - 1); // Return the last user
        }
        return null; // No users found
    }
}
