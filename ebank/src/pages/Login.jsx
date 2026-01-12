// src/pages/Login.jsx
import { useState } from "react";
import { useAuth } from "../hooks/useAuth";
import "../App.css";

export default function Login() {
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      await login(email, password);
      // ❌ NO navigate here
      // ✅ PublicRoute will redirect based on role
    } catch (err) {
      const message =
          err.response?.data?.message ||
          "Erreur lors de l’authentification";
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
      <div className="centered-container">
        <form onSubmit={handleLogin} className="card">
          <h2>Welcome Back</h2>
          <p>Enter your credentials to access your account</p>

          {error && <p style={{ color: "red" }}>{error}</p>}

          <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Email address"
              required
              className="input-field"
          />

          <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Password"
              required
              className="input-field"
          />

          <button type="submit" disabled={loading} className="btn">
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>
      </div>
  );
}
