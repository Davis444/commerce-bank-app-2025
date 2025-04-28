package com.backend.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XmlConfig {

    @Value("${users.xml.filepath}")
    private String usersFilePath;

    @Value("${links.xml.filepath}")
    private String linksFilePath;

    @Value("${logins.xml.filepath}")
    private String loginsFilePath;

    public String getUsersFilePath() {
        return usersFilePath;
    }

    public String getLinksFilePath() {
        return linksFilePath;
    }

    public String getLoginsFilePath() {
        return loginsFilePath;
    }
}