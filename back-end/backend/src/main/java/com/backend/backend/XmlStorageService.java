package com.backend.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.FileSystemResource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.io.InputStream;
import java.io.OutputStream;

@Service
public class XmlStorageService {

    @Value("${logins.xml.filepath}")
    private String loginsXmlPath;  // Property to configure file location

    public void saveLogin(LoginRequest loginRequest) {
        try {
            FileSystemResource loginsXmlResource = new FileSystemResource(loginsXmlPath);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;

            // Create new document if file doesn't exist
            if (!loginsXmlResource.exists()) {
                doc = builder.newDocument();
                Element root = doc.createElement("logins");
                doc.appendChild(root);
            } else {
                try (InputStream inputStream = loginsXmlResource.getInputStream()) {
                    doc = builder.parse(inputStream);
                }
            }

            // Create new login entry
            Element login = doc.createElement("login");

            Element email = doc.createElement("email");
            email.appendChild(doc.createTextNode(loginRequest.getEmail()));
            login.appendChild(email);

            Element password = doc.createElement("password");
            password.appendChild(doc.createTextNode(loginRequest.getPassword()));
            login.appendChild(password);

            doc.getDocumentElement().appendChild(login);

            // Write the updated document to file
            try (OutputStream outputStream = loginsXmlResource.getOutputStream()) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(outputStream);
                transformer.transform(source, result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}