import React, { useState } from "react";
import NavbarAdmin from "./NavbarAdmin";
import SidebarAdmin from "./SidebarAdmin";
import LoginHistory from "./LoginHistory";
import Messages from "./Messages";
import Users from "./Users";
import Documents from "./Documents";
import Logout from "../Auth/Logout";
import "bootstrap/dist/css/bootstrap.min.css";

const AdminDashboard = () => {
  const [activeSection, setActiveSection] = useState("Dashboard");

  return (
    <div className="d-flex">
      <SidebarAdmin onSelect={setActiveSection} />

      <div className="content w-100">
        <NavbarAdmin />

        <div className="container-fluid bg-white p-4 mt-5">
          {activeSection === "Dashboard" && (
            <h1
              className="mb-4 text-center fw-bold text-black"
              style={{ textShadow: "1px 1px 3px rgba(0, 0, 0, 0.1)" }}
            >
              WELCOME TO ADMIN DASHBOARD
            </h1>
          )}
          {activeSection === "Users" && <Users />}
          {activeSection === "Messages" && <Messages />}
          {activeSection === "Login History" && <LoginHistory />}
          {activeSection === "Documents" && <Documents />}
          {activeSection === "Logout" && <Logout />}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
