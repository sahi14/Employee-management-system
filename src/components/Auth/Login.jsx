import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

import "bootstrap/dist/css/bootstrap.min.css";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await axios.post("http://localhost:8080/users/login", {
        email,
        password,
      });
      let { access_token, role, refresh_token, UserId } = response.data;
      // console.log("access token", access_token);

      if (!access_token) {
        setError("Token not received. Please try again.");
        return;
      }

      // ✅ Check if the access token is expired
      const decodedToken = jwtDecode(access_token);
      const isExpired = decodedToken.exp * 1000 < Date.now();

      if (isExpired) {
        console.log("Access token expired. Refreshing...");
        const refreshResponse = await axios.post(
          "http://localhost:8080/api/refresh-token/refresh",
          { refreshToken: refresh_token }
        );

        access_token = refreshResponse.data.access_token;
      }

      // role format
      if (role.includes("ADMIN")) {
        role = "admin";
      } else if (role.includes("USER")) {
        role = "user";
      }

      // Store in localStorage
      localStorage.setItem("access_token", access_token);
      localStorage.setItem("role", role);
      localStorage.setItem("refresh_token", refresh_token);
      localStorage.setItem("UserId", UserId);

      // Redirect based on role
      if (role === "admin") {
        navigate("/admin/dashboard");
      } else if (role === "user") {
        navigate("/user/dashboard");
      } else {
        setError("Invalid role received from server");
      }
    } catch (err) {
      setError("Invalid credentials");
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div
        className="card shadow-lg p-4"
        style={{ width: "400px", borderRadius: "10px" }}
      >
        {/* EMPLOYEE MANAGEMENT SYSTEM Heading */}
        <h2
          className="text-center fw-bold text-dark mb-4"
          style={{ fontSize: "28px" }}
        >
          EMPLOYEE MANAGEMENT SYSTEM
        </h2>
        <h3 className="text-center mb-3">Login</h3>

        {error && <div className="alert alert-danger text-center">{error}</div>}

        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Email</label>
            <input
              type="email"
              className="form-control"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Password</label>
            <input
              type="password"
              className="form-control"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary w-100">
            Login
          </button>
        </form>

        <p className="text-center mt-3">
          Don't have an account?{" "}
          <a href="/register" className="text-decoration-none fw-semibold">
            Register
          </a>
        </p>
      </div>
    </div>
  );

  // return (
  //   <div className="login-container">
  //     <div className="login-box">
  //       <h2 className="text-center mb-3 fw-bold text-primary">
  //         EMPLOYEE MANAGEMENT SYSTEM
  //       </h2>
  //       <h2>Login</h2>
  //       {error && <p className="error-text">{error}</p>}
  //       <form onSubmit={handleLogin}>
  //         <div className="input-group">
  //           <label>Email</label>
  //           <input
  //             type="email"
  //             value={email}
  //             onChange={(e) => setEmail(e.target.value)}
  //             required
  //           />
  //         </div>
  //         <div className="input-group">
  //           <label>Password</label>
  //           <input
  //             type="password"
  //             value={password}
  //             onChange={(e) => setPassword(e.target.value)}
  //             required
  //           />
  //         </div>
  //         <button type="submit" className="login-button">
  //           Login
  //         </button>
  //       </form>

  //       {/* Register Button */}

  //       <p className="register-link">
  //         Don't have an account? <a href="/register">Register</a>
  //       </p>
  //     </div>
  //   </div>
  // );
};

export default Login;
