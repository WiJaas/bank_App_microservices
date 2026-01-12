import { createContext, useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import * as authService from "../services/authService";

export const AuthContext = createContext(null);

const extractRole = (decoded) => {
    if (decoded.role) return decoded.role.replace("ROLE_", "");

    if (decoded.roles?.length)
        return decoded.roles[0].replace("ROLE_", "");

    if (decoded.authorities?.length)
        return decoded.authorities[0].replace("ROLE_", "");

    if (decoded.scope)
        return decoded.scope.replace("ROLE_", "");

    return null;
};

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(null);
    const [role, setRole] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isAuthInitialized, setIsAuthInitialized] = useState(false);

    useEffect(() => {
        const storedToken = localStorage.getItem("token");

        if (storedToken) {
            const decoded = jwtDecode(storedToken);
            setToken(storedToken);
            setRole(extractRole(decoded));
            setIsAuthenticated(true);
        }

        setIsAuthInitialized(true);
    }, []);

    const login = async (email, password) => {
        const data = await authService.login(email, password);

        const token = data.token || data.accessToken || data.jwt;
        if (!token) throw new Error("No JWT returned");

        localStorage.setItem("token", token);

        const decoded = jwtDecode(token);
        console.log("JWT DECODED:", decoded);

        setToken(token);
        setRole(extractRole(decoded));
        setIsAuthenticated(true);
    };

    const logout = () => {
        localStorage.removeItem("token");
        setToken(null);
        setRole(null);
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider
            value={{
                token,
                role,
                isAuthenticated,
                isAuthInitialized,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};
