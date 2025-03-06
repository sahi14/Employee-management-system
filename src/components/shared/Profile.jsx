// import React, { useState } from "react";
// import axios from "axios";

// const Profile = ({ user, setUser }) => {
//   // const [showEditModal, setShowEditModal] = useState(false);
//   // const [updatedUser, setUpdatedUser] = useState({
//   //   firstName: user.firstName,
//   //   lastName: user.lastName,
//   //   email: user.email, // Added email field for updating
//   // });

//   // // Handle Edit Click
//   // const handleEditClick = () => {
//   //   setUpdatedUser({
//   //     firstName: user.firstName,
//   //     lastName: user.lastName,
//   //     email: user.email, // Ensure email is included
//   //   });
//   //   setShowEditModal(true);
//   // };

//   // // Handle Input Change
//   // const handleChange = (e) => {
//   //   const { name, value } = e.target;
//   //   setUpdatedUser((prev) => ({ ...prev, [name]: value }));
//   // };

//   // // Handle Update (API Call)
//   // const handleUpdate = async () => {
//   //   const userId = localStorage.getItem("UserId"); // Get User ID from local storage
//   //   const token = localStorage.getItem("access_token"); // Get Bearer Token

//   //   if (!userId || !token) {
//   //     console.error("User ID or Token not found in localStorage");
//   //     return;
//   //   }

//   //   try {
//   //     const response = await axios.post(
//   //       `http://localhost:8080/users/update-profile?id=${userId}`, // API URL
//   //       {
//   //         firstName: updatedUser.firstName,
//   //         lastName: updatedUser.lastName,
//   //         email: updatedUser.email, // Include email field
//   //       },
//   //       {
//   //         headers: {
//   //           "Content-Type": "application/json",
//   //           Authorization: `Bearer ${token}`, // Send Bearer token
//   //         },
//   //       }
//   //     );

//   //     setUser(response.data); // Update UI with new details
//   //     setShowEditModal(false);
//   //     console.log("User updated successfully:", response.data);
//   //   } catch (error) {
//   //     console.error("Error updating user:", error);
//   //   }
//   // };

//   return (
//     <div className="card shadow-sm p-3 bg-light">
//       <div className="card-body">
//         <div className="d-flex justify-content-between align-items-center">
//           <h5 className="card-title fw-bold">Profile</h5>
//           {/* <button className="btn btn-primary btn-sm" onClick={handleEditClick}>
//             Edit Details
//           </button> */}
//         </div>
//         <hr />

//         <p>
//           <strong>First Name:</strong> {user.firstName}
//         </p>
//         <p>
//           <strong>Last Name:</strong> {user.lastName}
//         </p>
//         <p>
//           <strong>Email:</strong> {user.email}
//         </p>
//         <p>
//           <strong>Role:</strong> {user.roleName}
//         </p>
//         <p>
//           <strong>Status:</strong>{" "}
//           <span className={`badge ${user.status ? "bg-success" : "bg-danger"}`}>
//             {user.status ? "Active" : "Inactive"}
//           </span>
//         </p>
//       </div>

//       {/* Edit Modal
//       {showEditModal && (
//         <div className="modal-overlay">
//           <div className="modal-container">
//             <div className="modal-header">
//               <h5>Edit Profile</h5>
//               <button onClick={() => setShowEditModal(false)}>×</button>
//             </div>
//             <div className="modal-body">
//               <label>First Name:</label>
//               <input
//                 type="text"
//                 name="firstName"
//                 value={updatedUser.firstName}
//                 onChange={handleChange}
//                 className="form-control"
//               />

//               <label>Last Name:</label>
//               <input
//                 type="text"
//                 name="lastName"
//                 value={updatedUser.lastName}
//                 onChange={handleChange}
//                 className="form-control"
//               />

//               <label>Email:</label>
//               <input
//                 type="email"
//                 name="email"
//                 value={updatedUser.email}
//                 onChange={handleChange}
//                 className="form-control"
//               />
//             </div>
//             <div className="modal-footer">
//               <button
//                 className="btn btn-secondary"
//                 onClick={() => setShowEditModal(false)}
//               >
//                 Cancel
//               </button>
//               <button className="btn btn-success" onClick={handleUpdate}>
//                 Save Changes
//               </button>
//             </div>
//           </div>
//         </div>
//       )} */}
//     </div>
//   );
// };

// export default Profile;

import React from "react";

const Profile = ({ user }) => {
  return (
    <div className="d-flex justify-content-center mt-4">
      <div
        className="card shadow-lg p-4 bg-white rounded"
        style={{ width: "400px" }}
      >
        <div className="card-body text-center">
          {/* Profile Icon */}
          <div className="mb-3">
            <i
              className="bi bi-person-circle"
              style={{ fontSize: "4rem", color: "#007bff" }}
            ></i>
          </div>

          <h4 className="fw-bold text-primary">Profile</h4>
          <hr />

          {/* Profile Details */}
          <div className="text-start">
            <p>
              <strong className="text-muted">First Name:</strong>{" "}
              {user.firstName}
            </p>
            <p>
              <strong className="text-muted">Last Name:</strong> {user.lastName}
            </p>
            <p>
              <strong className="text-muted">Email:</strong> {user.email}
            </p>
            <p>
              <strong className="text-muted">Role:</strong> {user.roleName}
            </p>
            <p>
              <strong className="text-muted">Status:</strong>{" "}
              <span
                className={`badge ${user.status ? "bg-success" : "bg-danger"}`}
                style={{ fontSize: "0.9rem" }}
              >
                {user.status ? "Active" : "Inactive"}
              </span>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
