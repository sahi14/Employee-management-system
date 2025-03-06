import React, { useState, useEffect, useRef } from "react";
import axios from "axios";

const UserMessages = () => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [userId, setUserId] = useState(null);
  const adminId = 32; // Admin ID
  const messagesEndRef = useRef(null);

  // ✅ Fetch logged-in user details
  useEffect(() => {
    const fetchUserId = async () => {
      try {
        const accessToken = localStorage.getItem("access_token");
        if (!accessToken) {
          console.error("No access token found, user not authenticated.");
          return;
        }

        const response = await axios.get(
          "http://localhost:8080/users/profile",
          {
            headers: { Authorization: `Bearer ${accessToken}` },
          }
        );

        setUserId(response.data.id);
      } catch (error) {
        console.error("Error fetching user:", error);
      }
    };

    fetchUserId();
  }, []);

  // ✅ Fetch messages
  const fetchMessages = async () => {
    if (!userId) return;

    try {
      const accessToken = localStorage.getItem("access_token");

      const receivedMessages = await axios.get(
        `http://localhost:8080/api/messages/received/${userId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );

      const sentMessages = await axios.get(
        `http://localhost:8080/api/messages/sent/${userId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );

      const allMessages = [...receivedMessages.data, ...sentMessages.data];

      const sortedMessages = allMessages.sort(
        (a, b) => new Date(a.sentAt) - new Date(b.sentAt)
      );

      setMessages(sortedMessages);
    } catch (error) {
      console.error("Error fetching messages:", error);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, [userId]);

  // ✅ Send message and immediately update state
  const sendMessage = async () => {
    if (!newMessage.trim() || !userId) return;

    try {
      const accessToken = localStorage.getItem("access_token");

      const payload = {
        senderId: userId,
        receiverId: adminId,
        message: newMessage,
      };

      const response = await axios.post(
        "http://localhost:8080/api/messages/send",
        payload,
        { headers: { Authorization: `Bearer ${accessToken}` } }
      );

      if (response.status === 201) {
        // ✅ Immediately update messages state
        setMessages((prevMessages) => [
          ...prevMessages,
          { ...payload, sentAt: new Date().toISOString(), senderId: userId },
        ]);
      }

      setNewMessage("");
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  // ✅ Auto-scroll to latest message
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="container mt-4">
      <h2 className="text-center">Chat with Admin</h2>
      <div className="row">
        <div className="col-md-12">
          <div
            className="border p-3 mb-3"
            style={{
              height: "400px",
              overflowY: "auto",
              backgroundColor: "#f8f9fa",
              display: "flex",
              flexDirection: "column",
            }}
          >
            {messages.length > 0 ? (
              messages.map((msg, index) => (
                <div
                  key={index}
                  className={`mb-2 p-2 text-white ${
                    msg.senderId === userId
                      ? "bg-primary text-end ms-auto" // Sent messages: Blue, Right
                      : "bg-secondary text-start me-auto" // Received messages: Gray, Left
                  }`}
                  style={{
                    borderRadius: "15px",
                    padding: "10px",
                    maxWidth: "75%",
                    wordWrap: "break-word",
                    alignSelf:
                      msg.senderId === userId ? "flex-end" : "flex-start",
                    textAlign: msg.senderId === userId ? "right" : "left",
                  }}
                >
                  {msg.message}
                </div>
              ))
            ) : (
              <p className="text-center text-muted">No messages yet</p>
            )}
            <div ref={messagesEndRef} /> {/* Auto-scroll */}
          </div>

          {/* ✅ Message Input */}
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
        </div>
      </div>
    </div>
  );
};

export default UserMessages;
