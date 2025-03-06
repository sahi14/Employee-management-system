import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

const Logout = () => {
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setShowModal(true);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    navigate("/login");
  };

  return (
    <>
      {showModal && (
        <div
          className="modal show d-block fade"
          tabIndex="-1"
          style={{ transition: "opacity 0.3s ease-in-out" }}
        >
          <div className="modal-dialog modal-dialog-centered">
            <div
              className="modal-content border-0 shadow-lg"
              style={{ borderRadius: "12px" }}
            >
              {/* Modal Header */}
              <div className="modal-header bg-dark text-white">
                <h5 className="modal-title fw-bold">CONFIRM LOGOUT</h5>
              </div>

              {/* Modal Body */}
              <div className="modal-body text-center">
                <p className="text-muted fs-5">
                  Are you sure you want to log out?
                </p>
              </div>

              {/* Modal Footer */}
              <div className="modal-footer d-flex justify-content-between">
                <button
                  className="btn btn-outline-secondary px-4"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
                <button className="btn btn-danger px-4" onClick={handleLogout}>
                  Logout
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );

  // return (
  //   <div className="d-flex justify-content-center align-items-center vh-100">
  //     {showModal && (
  //       <div className="modal show d-block" tabIndex="-1">
  //         <div className="modal-dialog">
  //           <div className="modal-content">
  //             <div className="modal-header">
  //               <h5 className="modal-title">Confirm Logout</h5>
  //             </div>
  //             <div className="modal-body">
  //               <p>Are you sure you want to log out?</p>
  //             </div>
  //             <div className="modal-footer">
  //               <button
  //                 className="btn btn-secondary"
  //                 onClick={() => setShowModal(false)}
  //               >
  //                 No
  //               </button>
  //               <button className="btn btn-danger" onClick={handleLogout}>
  //                 Yes, Logout
  //               </button>
  //             </div>
  //           </div>
  //         </div>
  //       </div>
  //     )}
  //   </div>
  // );
};

export default Logout;
