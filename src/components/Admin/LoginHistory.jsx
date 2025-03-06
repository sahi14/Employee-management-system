import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const LoginHistory = () => {
  const [history, setHistory] = useState([]);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("access_token");

    axios
      .get("http://localhost:8080/api/login-history/loginHistory", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        setHistory(response.data);
        setError(null);
      })
      .catch((error) => {
        console.error("Error fetching login history:", error);
        setError("Failed to fetch login history. Please try again.");
      });
  }, [navigate]);

  // Filtered history based on search term
  const filteredHistory = history.filter((item) =>
    item.userId.toString().includes(searchTerm)
  );

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2
          className="mb-4 text-center fw-bold text-black"
          style={{ textShadow: "1px 1px 3px rgba(0, 0, 0, 0.1)" }}
        >
          LOGIN HISTORY
        </h2>

        {/* Search Bar */}
        <div
          className="input-group"
          style={{ maxWidth: "500px", width: "100%" }}
        >
          <span className="input-group-text bg-dark text-white border-0 px-3 rounded-start">
            <i className="bi bi-search"></i>
          </span>
          <input
            type="text"
            className="form-control border-2 shadow-sm"
            placeholder="Search by User ID..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            style={{
              borderRadius: "0 20px 20px 0",
              backgroundColor: "#f8f9fa",
              borderColor: "#333",
            }}
          />
        </div>
      </div>
      {/* <h2>Login History</h2> */}
      {error && <div className="alert alert-danger">{error}</div>}
      <table className="table table-striped">
        <thead>
          <tr>
            <th>User ID</th>
            <th>Login Time</th>
          </tr>
        </thead>
        <tbody>
          {filteredHistory.length > 0 ? (
            filteredHistory.map((item, index) => (
              <tr key={index}>
                <td>{item.userId}</td>
                <td>{new Date(item.loginTime).toLocaleString()}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="2" className="text-center">
                No login history found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default LoginHistory;
