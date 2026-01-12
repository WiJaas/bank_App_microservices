import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext";
import ProtectedRoute from "./auth/ProtectedRoute";
import PublicRoute from "./auth/PublicRoute";
import RoleRoute from "./auth/RoleRoute";
import EmployeeDashboard from "./pages/EmployeeDashboard";
import CreateAccount from "./pages/CreateAccount";

import Login from "./pages/Login";
import AddClient from "./pages/AddClient";
import Dashboard from "./pages/Dashboard";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    {/* default redirect */}
                    <Route path="/" element={<PublicRoute>
                        <Login />
                    </PublicRoute>} />
                    {/* Public */}
                    <Route
                        path="/login"
                        element={
                            <PublicRoute>
                                <Login />
                            </PublicRoute>
                        }
                    />
                    <Route
                        path="/employees"
                        element={
                            <ProtectedRoute>
                                <RoleRoute allowedRole="AGENT_GUICHET">
                                    <EmployeeDashboard />
                                </RoleRoute>
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/employee/accounts/create"
                        element={
                            <ProtectedRoute roles={['EMPLOYEE']}>
                                <CreateAccount />
                            </ProtectedRoute>
                        }
                    />


                    {/* AGENT_GUICHET */}
                    <Route
                        path="/clients/new"
                        element={
                            <ProtectedRoute>
                                <RoleRoute allowedRole="AGENT_GUICHET">
                                    <AddClient />
                                </RoleRoute>
                            </ProtectedRoute>
                        }
                    />


                    {/* CLIENT */}
                    <Route
                        path="/dashboard"
                        element={
                            <ProtectedRoute>
                                <RoleRoute allowedRole="CLIENT">
                                    <Dashboard />
                                </RoleRoute>
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
