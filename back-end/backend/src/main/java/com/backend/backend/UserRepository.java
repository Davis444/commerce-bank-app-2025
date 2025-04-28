package com.backend.backend;

import org.springframework.stereotype.Repository;

import jakarta.xml.bind.JAXBException;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final XMLUtils xmlUtils;

    public UserRepository(XMLUtils xmlUtils) {
        this.xmlUtils = xmlUtils;
    }

    public Optional<User> findByEmail(String email) throws JAXBException {
        List<User> users = xmlUtils.readUsersFromXML();
        return users.stream()
                    .filter(user -> user.getEmail().equals(email))
                    .findFirst();
    }
    public Optional<User> findById(Long userId) throws JAXBException {
        List<User> users = xmlUtils.readUsersFromXML();
        return users.stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst();
    }
    
}
