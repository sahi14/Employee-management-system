import React, { useState, useEffect, useRef } from "react";
import axios from "axios";

const Messages = () => {
  const adminId = 32;
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [selectedUser, setSelectedUser] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [users, setUsers] = useState([]);
  const messagesEndRef = useRef(null);

  const fetchUsers = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/users/getAllUsers"
      );
      const filtered = response.data.filter((user) => user.id !== adminId);
      setUsers(filtered);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const fetchMessages = async () => {
    console.log("useerrrrr", selectedUser);
    if (!selectedUser) return;
    try {
      const receivedMessages = await axios.get(
        `http://localhost:8080/api/messages/received/${selectedUser.id}`
      );
      const sentMessages = await axios.get(
        `http://localhost:8080/api/messages/sent/${selectedUser.id}`
      );
      console.log("32423");

      const allMessages = [...receivedMessages.data, ...sentMessages.data];
      console.log("allMessages", allMessages);
      const sortedMessages = allMessages.sort(
        (a, b) => new Date(a.sentAt) - new Date(b.sentAt)
      );

      setMessages(sortedMessages);
    } catch (error) {
      console.error("Error fetching messages:", error);
    }
  };

  const sendMessage = async () => {
    if (!selectedUser || !newMessage.trim()) return;

    try {
      const payload = {
        senderId: adminId,
        receiverId: selectedUser.id,
        message: newMessage,
      };

      await axios.post("http://localhost:8080/api/messages/send", payload, {
        headers: { "Content-Type": "application/json" },
      });

      setNewMessage("");
      fetchMessages();
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  useEffect(() => {
    fetchUsers();
  }, []);

  useEffect(() => {
    fetchMessages();
  }, [selectedUser]);

  // Filter users based on search term
  const filteredUsers = users.filter((user) =>
    user.firstName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container mt-4">
      <h2
        className="mb-4 text-center fw-bold text-black"
        style={{ textShadow: "1px 1px 3px rgba(0, 0, 0, 0.1)" }}
      >
        MESSAGES
      </h2>
      <div className="row">
        <div
          className="col-md-4 border-end"
          style={{ height: "400px", overflowY: "auto" }}
        >
          {/* Search Bar
          <input
            type="text"
            className="form-control mb-2"
            placeholder="Search user to chat..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          /> */}

          {/* Search Bar */}
          <input
            type="text"
            className="form-control mb-3 px-4 py-2 rounded-pill shadow-sm"
            placeholder="Search User To Chat..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />

          {/* <ul className="list-group">
            {filteredUsers.map((user) => (
              <li
                key={user.id}
                className={`list-group-item ${
                  selectedUser?.id === user.id ? "active" : ""
                }`}
                onClick={() => setSelectedUser(user)}
                style={{ cursor: "pointer" }}
              >
                {user.firstName} {user.lastName}
              </li>
            ))}
          </ul> */}
          <ul className="list-group shadow-sm rounded">
            {filteredUsers.map((user) => (
              <li
                key={user.id}
                className={`list-group-item d-flex align-items-center justify-content-between ${
                  selectedUser?.id === user.id
                    ? "active bg-dark text-white"
                    : "bg-light"
                }`}
                onClick={() => setSelectedUser(user)}
                style={{
                  cursor: "pointer",
                  border: "1px solid rgba(0, 0, 0, 0.1)",
                  transition: "all 0.2s ease-in-out",
                }}
              >
                <span className="fw-bold">
                  {user.firstName} {user.lastName}
                </span>
                <i className="bi bi-person-circle text-secondary"></i>
              </li>
            ))}
          </ul>
        </div>

        <div className="col-md-8">
          {selectedUser ? (
            <>
              <h5>
                Chat with {selectedUser.firstName} {selectedUser.lastName}
              </h5>
              <div
                className="border p-3 mb-3"
                style={{
                  height: "300px",
                  overflowY: "auto",
                  backgroundColor: "#f8f9fa",
                  display: "flex",
                  flexDirection: "column",
                }}
              >
                {messages.length > 0 ? (
                  messages.map((msg) => (
                    <div
                      key={msg.id}
                      className={`mb-2 p-2 ${
                        msg.senderId === adminId
                          ? "bg-primary text-white text-end ms-auto"
                          : "bg-secondary text-white text-start me-auto"
                      }`}
                      style={{
                        borderRadius: "15px",
                        padding: "10px",
                        maxWidth: "75%",
                        wordWrap: "break-word",
                      }}
                    >
                      {msg.message}
                    </div>
                  ))
                ) : (
                  <p>No messages yet</p>
                )}
                <div ref={messagesEndRef} />
              </div>

              <div className="d-flex">
                <input
                  type="text"
                  className="form-control me-2"
                  placeholder="Type a message..."
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                />
                <button className="btn btn-primary" onClick={sendMessage}>
                  Send
                </button>
              </div>
            </>
          ) : (
            <p>Select a user to start chatting</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Messages;
