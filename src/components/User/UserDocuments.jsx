import React, { useEffect, useState } from "react";
import axios from "axios";

const UserDocuments = ({ userId }) => {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const id = localStorage.getItem("UserId");

  const API_BASE_URL = "http://localhost:8080/api";

  useEffect(() => {
    axios
      .post(`${API_BASE_URL}/financial-docs/get?userId=${id}`)
      .then((response) => {
        setDocuments(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching documents:", error);
        setLoading(false);
      });
  }, [userId]);

  // Function to handle document download
  const handleDownload = async (fileName) => {
    try {
      const response = await axios.get(
        `${API_BASE_URL}/financial-docs/download/${fileName}`,
        { responseType: "blob" }
      );
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (error) {
      console.error("Error downloading document:", error);
      alert("Failed to download document.");
    }
  };

  return (
    <div className="container mt-4">
      <h2>User Financial Documents</h2>

      {loading ? (
        <p>Loading...</p>
      ) : documents.length === 0 ? (
        <p>No documents found for this user.</p>
      ) : (
        <div className="row">
          {documents.map((doc) => (
            <div key={doc.documentId} className="col-md-4 mb-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">{doc.documentName}</h5>
                  <p className="card-text">
                    Uploaded At: {new Date(doc.uploadedAt).toLocaleString()}
                  </p>
                  <button
                    className="btn btn-success btn-sm"
                    onClick={() => handleDownload(doc.documentName)}
                  >
                    Download Document
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UserDocuments;
