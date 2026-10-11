// Backend URL. Local: Spring Boot on 8080. For production, change to your deployed backend URL.
window.API_BASE = window.API_BASE || (
  ["localhost", "127.0.0.1", ""].includes(location.hostname)
    ? "http://localhost:8080"
    : "https://YOUR-BACKEND-URL.onrender.com"
);
