import { Navigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function PublicRoute({ children }) {
  const { isAuthenticated, role, isAuthInitialized } = useAuth();

  // Wait until auth + role are fully ready
  if (!isAuthInitialized || (isAuthenticated && !role)) {
    return <Navigate to="/login" replace />;
  }

  if (isAuthenticated) {
    if (role === "AGENT_GUICHET") {
      return <Navigate to="/employees" replace />;
    }

    if (role === "CLIENT") {
      return <Navigate to="/dashboard" replace />;
    }
  }

  return children;
}
