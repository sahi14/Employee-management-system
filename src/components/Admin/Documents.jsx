import React, { useEffect, useState } from "react";
import axios from "axios";

const Documents = () => {
  const [users, setUsers] = useState([]);
  const [documents, setDocuments] = useState({});
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");

  const API_BASE_URL = "http://localhost:8080/api";

  useEffect(() => {
    axios
      .get("http://localhost:8080/users/getAllUsers")
      .then((response) => {
        setUsers(response.data);
        fetchDocumentsForUsers(response.data);
      })
      .catch((error) => console.error("Error fetching users:", error));
  }, []);

  // Fetch documents for each user
  const fetchDocumentsForUsers = (usersList) => {
    let docsMap = {};
    usersList.forEach((user) => {
      axios
        .post(`${API_BASE_URL}/financial-docs/get?userId=${user.id}`)
        .then((response) => {
          docsMap[user.id] = response.data;
          setDocuments((prevDocs) => ({
            ...prevDocs,
            [user.id]: response.data,
          }));
        })
        .catch((error) =>
          console.error(`Error fetching documents for user ${user.id}:`, error)
        );
    });
    setLoading(false);
  };

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

  // Ensure searchTerm is not empty before filtering
  const filteredUsers = users.filter((user) => {
    const firstName = user.name?.split(" ")[0]?.toLowerCase() || "";
    const email = user.email?.toLowerCase() || "";
    const searchLower = searchTerm.toLowerCase();

    return firstName.includes(searchLower) || email.includes(searchLower);
  });

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">Users & Their Financial Documents</h2>
        <input
          type="text"
          className="form-control w-25 px-4 py-2 rounded-pill shadow-sm"
          placeholder="Search User Documents"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      {/* <h2 className="mb-4">Users & Their Financial Documents</h2> */}

      {loading ? (
        <p>Loading...</p>
      ) : (
        filteredUsers.map((user) => (
          <div key={user.id} className="card mb-3">
            <div className="card-header">
              <h5 className="mb-0">
                {user.name} ({user.email})
              </h5>
            </div>
            <div className="card-body">
              {documents[user.id] && documents[user.id].length > 0 ? (
                <div className="row">
                  {documents[user.id].map((doc) => (
                    <div key={doc.id} className="col-md-4">
                      <div className="card">
                        <div className="card-body">
                          <h6 className="card-title">{doc.documentName}</h6>
                          <p className="card-text">
                            Uploaded At:{" "}
                            {new Date(doc.uploadedAt).toLocaleString()}
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
              ) : (
                <p>No financial documents available for this user.</p>
              )}
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default Documents;
