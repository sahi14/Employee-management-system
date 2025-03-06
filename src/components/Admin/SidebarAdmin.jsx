import { useState } from "react";
import {
  FaTachometerAlt,
  FaUsers,
  FaEnvelope,
  FaHistory,
  FaSignOutAlt,
  FaFileAlt,
} from "react-icons/fa";
import "bootstrap/dist/css/bootstrap.min.css";

const SidebarAdmin = ({ onSelect }) => {
  const [active, setActive] = useState("Dashboard");

  const handleSelect = (section) => {
    setActive(section);
    if (onSelect) {
      onSelect(section);
    }
  };

  return (
    <div
      className="d-flex flex-column bg-dark text-light vh-100 p-3 position-fixed"
      style={{ width: "250px" }}
    >
      <h2 className="text-center text-success mb-4">Admin Panel</h2>
      <ul className="nav flex-column">
        <li
          className={`nav-item p-2 ${
            active === "Dashboard" ? "bg-success rounded" : ""
          }`}
          onClick={() => handleSelect("Dashboard")}
          style={{ cursor: "pointer" }}
        >
          <FaTachometerAlt className="me-2" /> Dashboard
        </li>
        <li
          className={`nav-item p-2 ${
            active === "Users" ? "bg-success rounded" : ""
          }`}
          onClick={() => handleSelect("Users")}
          style={{ cursor: "pointer" }}
        >
          <FaUsers className="me-2" /> Users
        </li>
        <li
          className={`nav-item p-2 ${
            active === "Messages" ? "bg-success rounded" : ""
          }`}
          onClick={() => handleSelect("Messages")}
          style={{ cursor: "pointer" }}
        >
          <FaEnvelope className="me-2" /> Messages
        </li>
        <li
          className={`nav-item p-2 ${
            active === "Login History" ? "bg-success rounded" : ""
          }`}
          onClick={() => handleSelect("Login History", "/admin/login-history")}
          style={{ cursor: "pointer" }}
        >
          <FaHistory className="me-2" /> Login History
        </li>

        <li
          className={`nav-item p-2 ${
            active === "Documents" ? "bg-success rounded" : ""
          }`}
          onClick={() => handleSelect("Documents")}
          style={{ cursor: "pointer" }}
        >
          <FaFileAlt className="me-2" /> Documents
        </li>
        <li
          className={`nav-item p-2 ${
            active === "Logout" ? "bg-danger rounded" : ""
          }`}
          onClick={() => handleSelect("Logout")}
          style={{ cursor: "pointer" }}
        >
          <FaSignOutAlt className="me-2" /> Log Out
        </li>
      </ul>
    </div>
  );
};

export default SidebarAdmin;
