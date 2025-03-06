import React, { useState } from "react";
import NavbarAdmin from "../Admin/NavbarAdmin"; // Using the same Navbar
import SidebarUser from "./SidebarUser";
import UserMessages from "./UserMessages";
import UserDocuments from "./UserDocuments";
import Logout from "../Auth/Logout";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";

const UserDashboard = () => {
  const [activeSection, setActiveSection] = useState("Dashboard");

  return (
    <div className="d-flex">
      <SidebarUser onSelect={setActiveSection} />

      <div className="content w-100">
        <NavbarAdmin />
        <div className="container-fluid bg-white p-4 mt-5">
          {activeSection === "Dashboard" && <h1>Welcome to User Dashboard</h1>}
          {activeSection === "Messages" && <UserMessages />}
          {activeSection === "Documents" && <UserDocuments />}
          {activeSection === "Logout" && <Logout />}
        </div>
      </div>
    </div>
  );
};

export default UserDashboard;
