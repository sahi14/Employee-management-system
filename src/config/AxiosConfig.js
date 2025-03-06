import axios from "axios";

// Creating a custom axios instance
const axiosInstance = axios.create({
  baseURL: "http://localhost:8080", // Your backend URL
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("access_token")}`, // Pass JWT if needed
  },
});

export default axiosInstance;
