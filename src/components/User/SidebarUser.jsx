import { useState } from "react";
import {
  FaEnvelope,
  FaFileAlt,
  FaSignOutAlt,
  FaTachometerAlt,
} from "react-icons/fa";
import "bootstrap/dist/css/bootstrap.min.css";

const SidebarUser = ({ onSelect }) => {
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
      <h2 className="text-center text-primary mb-4">User Panel</h2>
      <ul className="nav flex-column">
        <li
          className={`nav-item p-2 ${
            active === "Dashboard" ? "bg-primary rounded" : ""
          }`}
          onClick={() => handleSelect("Dashboard")}
          style={{ cursor: "pointer" }}
        >
          <FaTachometerAlt className="me-2" /> Dashboard
        </li>

        <li
          className={`nav-item p-2 ${
            active === "Messages" ? "bg-primary rounded" : ""
          }`}
          onClick={() => handleSelect("Messages")}
          style={{ cursor: "pointer" }}
        >
          <FaEnvelope className="me-2" /> Messages
        </li>

        <li
          className={`nav-item p-2 ${
            active === "Documents" ? "bg-primary rounded" : ""
          }`}
          onClick={() => handleSelect("Documents")}
          style={{ cursor: "pointer" }}
        >
          <FaFileAlt className="me-2" /> Documents
        </li>

        {/* Logout Section */}
        <li
          className={`nav-item p-2 bg-danger rounded`}
          onClick={() => handleSelect("Logout")}
          style={{ cursor: "pointer" }}
        >
          <FaSignOutAlt className="me-2" /> Log Out
        </li>
      </ul>
    </div>
  );
};

export default SidebarUser;
