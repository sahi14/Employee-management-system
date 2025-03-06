import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./components/Auth/Login";
import Register from "./components/Auth/Register";
import AdminDashboard from "./components/Admin/Dashboard";
import UserDashboard from "./components/User/Dashboard";
import Profile from "./components/shared/Profile";
import Messages from "./components/Admin/Messages";
import LoginHistory from "./components/Admin/LoginHistory";
import Documents from "./components/Admin/Documents";
import UserDocuments from "./components/User/UserDocuments";
import Logout from "./components/Auth/Logout";
import ApproveUser from "./components/Admin/ApproveUser";
import UserTableAction from "./components/Admin/Users"; // Import Users component for Admin

const ProtectedRoute = ({ element, allowedRoles }) => {
  const role = localStorage.getItem("role");

  return allowedRoles.includes(role) ? element : <Navigate to="/login" />;
};

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Profile Route for Both Admin & User */}
        <Route
          path="/profile"
          element={
            <ProtectedRoute
              element={<Profile />}
              allowedRoles={["admin", "user"]}
            />
          }
        />

        {/* Admin Routes */}
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute
              element={<AdminDashboard />}
              allowedRoles={["admin"]}
            />
          }
        />
        <Route
          path="/admin/users" // Admin users page
          element={
            <ProtectedRoute
              element={<UserTableAction />}
              allowedRoles={["admin"]}
            />
          }
        />
        {/* <Route
          path="/admin/add-user" // New route for adding users
          element={
            <ProtectedRoute element={<AddUser />} allowedRoles={["admin"]} />
          }
        /> */}

        {/* User Routes */}
        <Route
          path="/user/dashboard"
          element={
            <ProtectedRoute
              element={<UserDashboard />}
              allowedRoles={["user"]}
            />
          }
        />

        <Route
          path="/admin/messages"
          element={
            <ProtectedRoute element={<Messages />} allowedRoles={["admin"]} />
          }
        />

        <Route
          path="/admin/login-history"
          element={
            <ProtectedRoute
              element={<LoginHistory />}
              allowedRoles={["admin"]}
            />
          }
        />

        <Route
          path="/admin/documents"
          element={
            <ProtectedRoute element={<Documents />} allowedRoles={["admin"]} />
          }
        />

        <Route path="/logout" element={<Logout />} />

        {/* Standalone Approval Page (No authentication required) */}
        <Route path="/approve-user" element={<ApproveUser />} />

        {/* Fallback route */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
};

export default App;
