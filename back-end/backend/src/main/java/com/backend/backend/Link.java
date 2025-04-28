package com.backend.backend;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Link {

    private Long id;
    private String url;
    private int responseCode;
    private String responseMessage;
    private String sslProtocol;
    private String sslCipher;
    private String certificateInfo;
    private String userId;

    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id != null && id <= 0) { // Ensure ID is positive
            throw new IllegalArgumentException("ID must be positive and non-null.");
        }
        this.id = id;
    }

    @XmlElement
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url == null || url.isBlank()) { // Validate non-null and non-blank URL
            throw new IllegalArgumentException("URL cannot be null or blank.");
        }
        this.url = url;
    }

    @XmlElement
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        if (responseCode < 0) { // Validate response code is non-negative
            throw new IllegalArgumentException("Response code must be non-negative.");
        }
        this.responseCode = responseCode;
    }

    @XmlElement
    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @XmlElement
    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    @XmlElement
    public String getSslCipher() {
        return sslCipher;
    }

    public void setSslCipher(String sslCipher) {
        this.sslCipher = sslCipher;
    }

    @XmlElement
    public String getCertificateInfo() {
        return certificateInfo;
    }

    public void setCertificateInfo(String certificateInfo) {
        this.certificateInfo = certificateInfo;
    }

    @XmlElement
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (userId == null || userId.isBlank()) { // Validate non-null and non-blank user ID
            throw new IllegalArgumentException("User ID cannot be null or blank.");
        }
        this.userId = userId;
    }

    // Optional: Add a convenience toString() method for debugging purposes
    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", sslProtocol='" + sslProtocol + '\'' +
                ", sslCipher='" + sslCipher + '\'' +
                ", certificateInfo='" + certificateInfo + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}