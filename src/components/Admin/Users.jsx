import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaUpload } from "react-icons/fa";
import "bootstrap/dist/css/bootstrap.min.css";

const Users = () => {
  const [users, setUsers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [newUser, setNewUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    roleName: "USER",
    password: "",
  });
  const [editUser, setEditUser] = useState(null);
  const [updatedUser, setUpdatedUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    roleName: "",
    status: false,
  });
  const [showEditModal, setShowEditModal] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/users/getAllUsers"
      );
      setUsers(response.data);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const approveUser = async (userId) => {
    try {
      await axios.post(`http://localhost:8080/users/approve?id=${userId}`);
      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === userId ? { ...user, status: true } : user
        )
      );
    } catch (error) {
      console.error("Error approving user:", error);
    }
  };

  const deactivateUser = async (userId) => {
    try {
      await axios.post(`http://localhost:8080/users/deactivate?id=${userId}`);
      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === userId ? { ...user, status: false } : user
        )
      );
    } catch (error) {
      console.error("Error deactivating user:", error);
    }
  };

  const handleChange = (e) => {
    setNewUser({ ...newUser, [e.target.name]: e.target.value });
  };

  const handleAddUser = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/users/register",
        {
          userDto: {
            firstName: newUser.firstName,
            lastName: newUser.lastName,
            email: newUser.email,
            roleName: newUser.roleName,
            status: false,
          },
          password: newUser.password,
        }
      );

      setUsers([...users, response.data]);
      setShowModal(false);
      setNewUser({
        firstName: "",
        lastName: "",
        email: "",
        roleName: "",
        password: "",
      });
    } catch (error) {
      console.error("Error registering user:", error);
    }
  };

  // Handle file selection for importing users
  const handleFileChange = (event) => {
    console.log("File selected:", event.target.files[0]);
    setSelectedFiles(event.target.files[0]);
  };

  // Upload Excel file to import users
  const handleImportUsers = async () => {
    if (!selectedFiles) {
      alert("Please select a file first.");
      return;
    }
    console.log("Uploading file:", selectedFiles.name);

    const formData = new FormData();
    formData.append("file", selectedFiles);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/excel/upload",
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      );

      if (response.status === 200) {
        let alertMessage = "Users imported successfully.";

        if (response.data.duplicates && response.data.duplicates.length > 0) {
          alertMessage += `\nThe following emails were not imported due to duplication:\n${response.data.duplicates.join(
            ", "
          )}`;
        }

        alert(alertMessage);
        fetchUsers();
        setSelectedFiles(null);
      }
    } catch (error) {
      console.error("Error importing users:", error);
      alert("Failed to import users. Please try again.");
    }
  };

  const handleEditClick = (user) => {
    console.log("Edit Clicked for:", user);
    setEditUser(user);
    setUpdatedUser({
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      roleName: user.roleName,
      status: user.status,
    });
    setShowEditModal(true);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUpdatedUser({
      ...updatedUser,
      [name]: name === "status" ? JSON.parse(value) : value,
    });
  };

  const handleUpdate = async () => {
    try {
      await axios.post(
        `http://localhost:8080/users/update?id=${editUser.id}`,
        updatedUser,
        { headers: { "Content-Type": "application/json" } }
      );

      setUsers(
        users.map((user) =>
          user.id === editUser.id ? { ...user, ...updatedUser } : user
        )
      );

      setShowEditModal(false);
    } catch (error) {
      console.error("Error updating user:", error);
    }
  };

  const handleDocumentChange = (event, userId) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    uploadDocument(formData, userId);
  };

  const uploadDocument = async (formData, userId, fetchDocuments) => {
    try {
      const response = await axios.post(
        `http://localhost:8080/api/financial-docs/upload?userId=${userId}`,
        formData,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      alert(`Document uploaded successfully for User ID: ${userId}`);

      // Fetch updated documents after upload
      fetchDocuments(userId);
    } catch (error) {
      console.error("Error uploading document:", error);
      alert("Failed to upload document.");
    }
  };

  // Filter users based on email search term
  const filteredUsers = users.filter((user) =>
    user.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container mt-4">
      <h2
        className="mb-4 text-center fw-bold text-black"
        style={{ textShadow: "1px 1px 3px rgba(0, 0, 0, 0.1)" }}
      >
        USER MANAGEMENT
      </h2>
      {/* <h2 className="mb-3 text-center">USER MANAGEMENT</h2> */}

      <div className="d-flex justify-content-between align-items-center mb-3">
        {/* Search Bar (Left-Aligned) */}
        <input
          type="text"
          className="form-control px-3 py-1 border border-3 rounded-pill shadow-sm"
          placeholder="Search User Email"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{
            maxWidth: "1000px",
            minWidth: "250px",
            flex: "1",
            borderColor: "#000",
            color: "#000",
            fontWeight: "500",
          }}
        />

        {/* Buttons (Right-Aligned) */}
        <div className="d-flex gap-2">
          <button
            className="btn btn-primary"
            onClick={() => setShowModal(true)}
          >
            Add User
          </button>

          {/* Import Users Button & Upload */}
          <input
            type="file"
            className="d-none"
            id="fileInput"
            onChange={handleFileChange}
          />
          <label className="btn btn-success" htmlFor="fileInput">
            Import Users
          </label>

          {selectedFiles && (
            <button className="btn btn-info" onClick={handleImportUsers}>
              Upload
            </button>
          )}
        </div>
      </div>

      {/* <div className="d-flex justify-content-start mb-3">
        <input
          type="text"
          className="form-control w-50 px-3 py-1 border rounded-pill shadow-sm"
          placeholder="Search by Email..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{ maxWidth: "1000px", minWidth: "250px" }}
        />
        <button
          className="btn btn-primary me-2"
          onClick={() => setShowModal(true)}
        >
          Add User
        </button>

        <input
          type="file"
          className="d-none"
          id="fileInput"
          onChange={handleFileChange}
        />
        <label className="btn btn-success me-2" htmlFor="fileInput">
          Import Users
        </label>

        {selectedFiles && (
          <button className="btn btn-info" onClick={handleImportUsers}>
            Upload
          </button>
        )}
      </div> */}

      <div className="table-container">
        <table className="table table-striped table-bordered text-center">
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
              <th>Actions</th>
              <th>Upload Document</th>
            </tr>
          </thead>
          <tbody>
            {filteredUsers.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.firstName}</td>
                <td>{user.lastName}</td>
                <td>{user.email}</td>
                <td>{user.roleName}</td>
                <td>
                  <span
                    className={`badge ${
                      user.status ? "bg-success" : "bg-danger"
                    }`}
                  >
                    {user.status ? "Active" : "Inactive"}
                  </span>
                </td>
                <td>
                  {user.status ? (
                    <button
                      className="btn btn-warning me-2"
                      onClick={() => deactivateUser(user.id)}
                    >
                      Deactivate
                    </button>
                  ) : (
                    <button
                      className="btn btn-success me-2"
                      onClick={() => approveUser(user.id)}
                    >
                      Approve
                    </button>
                  )}
                  <button
                    className="btn btn-primary"
                    onClick={() => handleEditClick(user)}
                  >
                    Edit
                  </button>
                </td>
                <td>
                  <label className="btn btn-outline-primary btn-sm">
                    <FaUpload />
                    <input
                      type="file"
                      hidden
                      onChange={(event) => handleDocumentChange(event, user.id)}
                    />
                  </label>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showEditModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h4 className="mb-3">Edit User</h4>

            <div className="mb-2">
              <input
                type="text"
                className="form-control"
                placeholder="First Name"
                name="firstName"
                value={updatedUser.firstName}
                onChange={handleInputChange}
              />
            </div>
            <div className="mb-2">
              <input
                type="text"
                className="form-control"
                placeholder="Last Name"
                name="lastName"
                value={updatedUser.lastName}
                onChange={handleInputChange}
              />
            </div>
            <div className="mb-2">
              <input
                type="email"
                className="form-control"
                placeholder="Email"
                name="email"
                value={updatedUser.email}
                onChange={handleInputChange}
              />
            </div>
            <div className="mb-3">
              <select
                className="form-control"
                name="roleName"
                value={updatedUser.roleName}
                onChange={handleInputChange}
              >
                <option value="USER">USER</option>
                <option value="ADMIN">ADMIN</option>
              </select>
            </div>
            <div className="d-flex justify-content-between">
              <button className="btn btn-success" onClick={handleUpdate}>
                Update
              </button>
              <button
                className="btn btn-danger"
                onClick={() => setShowEditModal(false)}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Custom Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h4 className="mb-3">Add New User</h4>
            <div className="mb-2">
              <input
                type="text"
                className="form-control"
                placeholder="First Name"
                name="firstName"
                value={newUser.firstName}
                onChange={handleChange}
              />
            </div>
            <div className="mb-2">
              <input
                type="text"
                className="form-control"
                placeholder="Last Name"
                name="lastName"
                value={newUser.lastName}
                onChange={handleChange}
              />
            </div>
            <div className="mb-2">
              <input
                type="email"
                className="form-control"
                placeholder="Email"
                name="email"
                value={newUser.email}
                onChange={handleChange}
              />
            </div>
            <div className="mb-2">
              <input
                type="password"
                className="form-control"
                placeholder="Password"
                name="password"
                value={newUser.password}
                onChange={handleChange}
              />
            </div>
            <div className="mb-3">
              <select
                className="form-control"
                name="roleName"
                value={newUser.roleName}
                onChange={handleChange}
              >
                <option value="USER">USER</option>
                <option value="ADMIN">ADMIN</option>
              </select>
            </div>
            <div className="d-flex justify-content-between">
              <button className="btn btn-success" onClick={handleAddUser}>
                Register
              </button>
              <button
                className="btn btn-danger"
                onClick={() => setShowModal(false)}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      <style>
        {`
          .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
          }
          .modal-content {
            background: white;
            padding: 20px;
            border-radius: 8px;
            width: 350px;
          }
        `}
      </style>
    </div>
  );
};

export default Users;
