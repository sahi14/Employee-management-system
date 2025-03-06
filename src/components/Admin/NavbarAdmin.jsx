import { useState, useEffect, useRef } from "react";
import axios from "axios";
import { FaUserCircle } from "react-icons/fa";
import Profile from "../shared/Profile"; // Ensure correct import path
import "bootstrap/dist/css/bootstrap.min.css";

const NavbarAdmin = () => {
  const [showDropdown, setShowDropdown] = useState(false);
  const [user, setUser] = useState(null);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/users/profile",
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("access_token")}`,
            },
          }
        );
        setUser(response.data);
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };

    fetchUserProfile();
  }, []);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <>
      {/* Fixed Navbar */}
      <nav className="navbar navbar-dark bg-dark fixed-top">
        <div className="container-fluid d-flex justify-content-between align-items-center">
          <h2
            className="mb-4 text-center fw-bold text-white"
            style={{ textShadow: "1px 1px 3px rgba(255, 255, 255, 0.2)" }}
          >
            EMPLOYEE MANAGEMENT SYSTEM
          </h2>

          {/* <h2 className="text-white m-0">EMPLOYEE MANAGEMENT SYSTEM</h2> */}

          {/* Profile Icon */}
          <div ref={dropdownRef}>
            <button
              className="btn btn-outline-light"
              onClick={() => setShowDropdown(!showDropdown)}
            >
              <FaUserCircle size={24} />
            </button>
          </div>
        </div>
      </nav>

      {/* Profile Dropdown Fullscreen Container */}
      {showDropdown && user && (
        <div
          className="position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-75 d-flex justify-content-center align-items-center"
          style={{ zIndex: 1050 }}
        >
          <div
            className="bg-white p-4 rounded shadow-lg text-center"
            style={{ width: "400px" }}
          >
            <h3 className="mb-3">Profile Details</h3>
            <Profile user={user} />
            <button
              className="btn btn-danger mt-3"
              onClick={() => setShowDropdown(false)}
            >
              Close
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default NavbarAdmin;
