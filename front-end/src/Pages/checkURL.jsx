import React, { useState, useEffect } from "react";
import "../index.css";
import Background from "../Components/background";
import axios from "axios";

const CheckURL = () => {
  const [url, setUrl] = useState("");
  const [certificateInfo, setCertificateInfo] = useState(null);
  const [responseCode, setResponseCode] = useState(null);
  const [responseHeaders, setResponseHeaders] = useState(null);
  const [sslStatus, setSslStatus] = useState(null);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [savedLinks, setSavedLinks] = useState([]);
  const [showSavedLinks, setShowSavedLinks] = useState(false);
  const [editingId, setEditingId] = useState(null); // Tracks which URL is being edited
  const [newUrl, setNewUrl] = useState(""); // New URL entered during editing

  // Fetch saved links on page load
  useEffect(() => {
    fetchSavedLinks();
  }, []);

  const resetStatus = () => {
    setError(null);
    setSuccess(null);
    setCertificateInfo(null);
    setResponseCode(null);
    setResponseHeaders(null);
    setSslStatus(null);
  };

  const fetchSavedLinks = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/links/list");
      setSavedLinks(response.data);
    } catch (error) {
      console.error("Error fetching saved URLs:", error);
      setError("Failed to fetch saved URLs.");
    }
  };

  const handleScrape = async () => {
    resetStatus();
    if (!isValidURL(url)) {
      setError("Invalid URL. Please enter a valid URL starting with http:// or https://.");
      return;
    }
    try {
      const response = await axios.post("http://localhost:8080/api/links/scrape", null, { params: { url } });
      const { sslProtocol, sslCipher, certificateInfo, responseCode, responseMessage } = response.data;
      setSslStatus(`${sslProtocol} (${sslCipher})`);
      setCertificateInfo(certificateInfo);
      setResponseCode(responseCode);
      setResponseHeaders(responseMessage);
      setSuccess("Scrape successful!");
    } catch (error) {
      console.error("Error scraping website:", error);
      setError("An error occurred while scraping the URL. Please try again.");
    }
  };

  const handleSave = async () => {
    resetStatus();
    if (!isValidURL(url)) {
      setError("Invalid URL. Cannot save.");
      return;
    }
    if (savedLinks.some((link) => link.url === url)) {
      setError("This URL is already saved.");
      return;
    }
    try {
      const response = await axios.post("http://localhost:8080/api/links/save", null, { params: { url } });
      setSuccess(`URL saved: ${response.data.url}`);
      fetchSavedLinks();
    } catch (error) {
      console.error("Error saving URL:", error);
      setError("Failed to save the URL.");
    }
  };

  const handleEditSubmit = async () => {
    if (!editingId || !isValidURL(newUrl)) {
      setError("Invalid ID or URL.");
      return;
    }
    try {
      await axios.put("http://localhost:8080/api/links/edit", null, {
        params: { id: editingId, newUrl },
      });
      setEditingId(null); // Reset editing state
      setNewUrl("");
      fetchSavedLinks(); // Refresh links list
      setSuccess("URL updated successfully!");
    } catch (error) {
      console.error("Error editing URL:", error);
      setError("Failed to edit the URL.");
    }
  };

  const handleDelete = async (id) => {
    if (!id) {
      setError("Invalid ID. Cannot delete.");
      return;
    }
    try {
      await axios.delete(`http://localhost:8080/api/links/delete`, {
        params: { id }, // Ensure `id` is sent correctly
      });
      setSuccess("URL deleted successfully!");
      fetchSavedLinks();
    } catch (error) {
      console.error("Error deleting URL:", error);
      setError("Failed to delete the URL.");
    }
  };

  const handleRescan = async (id) => {
    try {
      const response = await axios.post("http://localhost:8080/api/links/rescan", null, { params: { id } });
      const { sslProtocol, sslCipher, certificateInfo, responseCode, responseMessage } = response.data;
      setSslStatus(`${sslProtocol} (${sslCipher})`);
      setCertificateInfo(certificateInfo);
      setResponseCode(responseCode);
      setResponseHeaders(responseMessage);
      setSuccess(`Rescan complete for URL: ${response.data.url}`);
    } catch (error) {
      console.error("Error rescanning URL:", error);
      setError("Failed to rescan the URL.");
    }
  };

  const isValidURL = (url) => {
    const pattern = new RegExp(
      "^(https?:\\/\\/)" +
        "((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.?)+[a-z]{2,}|" +
        "((\\d{1,3}\\.){3}\\d{1,3}))" +
        "(\\:\\d+)?(\\/[-a-z\\d%@_.~+&:]*)*" +
        "(\\?[;&a-z\\d%@_.,~+&:=-]*)?" +
        "(\\#[-a-z\\d_]*)?$",
      "i"
    );
    return pattern.test(url);
  };

  return (
    <div
      style={{
        position: "relative",
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-start",
        minHeight: "100vh",
        padding: "50px 20px",
        background: "linear-gradient(135deg, #0f172a, #15202b)",
        backgroundSize: "400% 400%",
        animation: "gradientBackground 6s ease infinite",
        overflow: "hidden",
      }}
    >
      <Background />
      <div
        style={{
          backgroundColor: "rgba(255, 255, 255, 0.05)",
          boxShadow: "0px 4px 15px rgba(0, 0, 0, 0.4)",
          padding: "32px",
          borderRadius: "20px",
          width: "100%",
          maxWidth: "600px",
          backdropFilter: "blur(10px)",
          zIndex: 1,
          border: "1px solid rgba(255, 255, 255, 0.15)",
        }}
      >
        <h2
          style={{
            fontSize: "28px",
            color: "white",
            textAlign: "center",
            marginBottom: "25px",
            letterSpacing: "1.5px",
            fontWeight: "500",
          }}
        >
          Web Scraper
        </h2>
  
        {/* URL Input */}
        <input
          type="text"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          placeholder="Enter website URL (http:// or https://)"
          style={{
            width: "100%",
            padding: "12px",
            borderRadius: "10px",
            marginBottom: "15px",
            border: "1px solid rgba(255, 255, 255, 0.2)",
            backgroundColor: "rgba(0, 0, 0, 0.3)",
            color: "white",
            fontSize: "14px",
          }}
        />
  
        {/* Buttons */}
        <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginTop: "20px",
          width: "100%",
        }}
        >      
        <button
          onClick={handleScrape}
          className="primary-btn"
          style={{
            backgroundColor: "#2563eb",
            color: "white",
            padding: "10px 20px",
            borderRadius: "60px",
            border: "none",
            fontSize: "16px",
            cursor: "pointer",
            marginTop: "10px",
            transition: "transform 0.2s",
          }}
          onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
          onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
        >
          Scrape Website
        </button>
        <button
          onClick={handleSave}
          className="secondary-btn"
          style={{
            backgroundColor: "#10b981",
            color: "white",
            padding: "10px 30px",
            borderRadius: "60px",
            border: "none",
            fontSize: "16px",
            cursor: "pointer",
            marginTop: "10px",
            transition: "transform 0.2s",
          }}
          onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
          onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
        >
          Save URL
        </button>
        </div>
        {/* Display Success/Error */}
        {error && (
          <p style={{ color: "#ef4444", textAlign: "center", marginTop: "10px", fontSize: "14px" }}>
            {error}
          </p>
        )}
        {success && (
          <p style={{ color: "#10b981", textAlign: "center", marginTop: "10px", fontSize: "14px" }}>
            {success}
          </p>
        )}
  
        {/* Scraped Information */}
        {certificateInfo && (
          <div style={{ marginTop: "20px", color: "white" }}>
            <h3 style={{ fontSize: "18px", marginBottom: "10px" }}>Scraped Information</h3>
            <p>
              <strong>SSL Status:</strong> {sslStatus}
            </p>
            <p>
              <strong>Certificate:</strong> {certificateInfo}
            </p>
            <p>
              <strong>Response Code:</strong> {responseCode}
            </p>
            <p>
              <strong>Headers:</strong> {responseHeaders}
            </p>
          </div>
        )}
  
        {/* Dropdown for Saved Links */}
        <div style={{ marginTop: "30px" }}>
          <button
            onClick={() => setShowSavedLinks(!showSavedLinks)}
            className="secondary-btn"
            style={{
              width: "100%",
              backgroundColor: "#2563eb",
              color: "white",
              padding: "12px 30px",
              borderRadius: "8px",
              border: "none",
              fontSize: "16px",
              cursor: "pointer",
              marginBottom: "15px",
              transition: "transform 0.2s",
            }}
            onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
            onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
          >
            {showSavedLinks ? "Hide Saved Links" : "Show Saved Links"}
          </button>
  
          {showSavedLinks && (
            <ul style={{ listStyle: "none", padding: 0, marginTop: "20px", color: "white" }}>
              {savedLinks.length === 0 ? (
                <li
                  style={{
                    textAlign: "center",
                    marginTop: "10px",
                    fontSize: "14px",
                    color: "rgba(255, 255, 255, 0.8)",
                  }}
                >
                  No saved URLs yet.
                </li>
              ) : (
                savedLinks.map((link) => (
                  <li
                    key={link.id}
                    style={{
                      marginBottom: "10px",
                      padding: "10px",
                      borderRadius: "8px",
                      backgroundColor: "rgba(255, 255, 255, 0.1)",
                    }}
                  >
                    <p style={{ fontSize: "16px", color: "white" }}>{link.url}</p>
                    <div style={{ marginTop: "5px", display: "flex", justifyContent: "space-between" }}>
                      <button
                        onClick={() => {
                          setEditingId(link.id);
                          setNewUrl(link.url);
                        }}
                        className="small-btn"
                        style={{
                          backgroundColor: "#10b981",
                          padding: "8px 15px",
                          borderRadius: "6px",
                          border: "none",
                          cursor: "pointer",
                          color: "white",
                          transition: "transform 0.2s",
                        }}
                        onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
                        onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleRescan(link.id)}
                        className="small-btn"
                        style={{
                          backgroundColor: "#2563eb",
                          padding: "8px 15px",
                          borderRadius: "6px",
                          border: "none",
                          cursor: "pointer",
                          color: "white",
                          transition: "transform 0.2s",
                        }}
                        onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
                        onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
                      >
                        Rescan
                      </button>
                      <button
                        onClick={() => handleDelete(link.id)}
                        className="small-btn danger-btn"
                        style={{
                          backgroundColor: "#ef4444",
                          padding: "8px 15px",
                          borderRadius: "6px",
                          border: "none",
                          cursor: "pointer",
                          color: "white",
                          transition: "transform 0.2s",
                        }}
                        onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
                        onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
                      >
                        Delete
                      </button>
                    </div>
                  </li>
                ))
              )}
            </ul>
          )}
        </div>
        {/* Edit Modal */}
        {editingId && (
        <div
          style={{
            position: "fixed",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            backgroundColor: "rgba(0, 0, 0, 0.8)",
            padding: "30px",
            borderRadius: "15px",
            textAlign: "center",
            zIndex: 999,
            boxShadow: "0px 5px 20px rgba(0, 0, 0, 0.5)",
            width: "90%",
            maxWidth: "400px",
          }}
        >
          <h3 style={{ color: "white", marginBottom: "20px", fontSize: "20px" }}>Edit URL</h3>
          <input
            type="text"
            value={newUrl}
            onChange={(e) => setNewUrl(e.target.value)} // Update new URL as user types
            placeholder="Enter new URL"
            style={{
              width: "100%",
              padding: "12px",
              borderRadius: "8px",
              marginBottom: "20px",
              border: "1px solid rgba(255, 255, 255, 0.3)",
              backgroundColor: "rgba(255, 255, 255, 0.1)",
              color: "white",
              fontSize: "14px",
            }}
          />
          <div style={{ display: "flex", justifyContent: "space-between", gap: "10px" }}>
            <button
              onClick={handleEditSubmit} // Submit the new URL
              style={{
                backgroundColor: "#2563eb",
                color: "white",
                padding: "10px 20px",
                borderRadius: "8px",
                border: "none",
                fontSize: "16px",
                cursor: "pointer",
                transition: "transform 0.2s",
                flex: "1",
              }}
              onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
              onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
            >
              Submit
            </button>
            <button
              onClick={() => {
                setEditingId(null); // Cancel editing
                setNewUrl("");
              }}
              style={{
                backgroundColor: "#ef4444",
                color: "white",
                padding: "10px 20px",
                borderRadius: "8px",
                border: "none",
                fontSize: "16px",
                cursor: "pointer",
                transition: "transform 0.2s",
                flex: "1",
              }}
              onMouseOver={(e) => (e.target.style.transform = "scale(1.05)")}
              onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
            >
              Cancel
            </button>
          </div>
        </div>
        )};
        </div>
  </div>
  )
}
export default CheckURL;
