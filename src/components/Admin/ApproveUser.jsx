import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { Modal, Button } from "react-bootstrap";

const ApproveUser = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [show, setShow] = useState(true);
  const [id, setId] = useState("");

  useEffect(() => {
    // Extract email from query params
    const queryParams = new URLSearchParams(location.search);
    const id = queryParams.get("id");
    if (id) {
      setId(id);
    }
  }, [location]);

  const handleApprove = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/users/approve",
        null,
        {
          params: { id }, // Pass email as query param
        }
      );

      alert("User approved successfully!");
      setShow(false);
      navigate("/admin/users"); // Redirect to Users page after approval
    } catch (error) {
      console.error("Error approving user:", error);
      alert("Failed to approve user. Please try again.");
    }
  };

  return (
    <Modal show={show} onHide={() => navigate("/admin/users")} centered>
      <Modal.Header closeButton>
        <Modal.Title>Confirm Approval</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
          Are you sure you want to approve <strong>{id}</strong>?
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={() => navigate("/admin/users")}>
          Cancel
        </Button>
        <Button variant="primary" onClick={handleApprove}>
          Yes, Approve
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ApproveUser;
