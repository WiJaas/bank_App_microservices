import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function NavbarEmployee() {

    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };

    return (
        <header className="bg-slate-900 text-white">
            <div className="max-w-7xl mx-auto px-6 py-3 flex items-center justify-between">

                {/* Left side */}
                <div className="flex items-center gap-4">
          <span className="text-lg font-semibold">
            Wijdane Banque — Espace Agent
          </span>

                    <nav className="flex items-center gap-4 text-sm">
                        <Link to="/employees" className="hover:underline">
                            Liste des clients
                        </Link>

                      
                        
                    </nav>
                </div>

                {/* Right side */}
                <div className="flex items-center gap-3 text-sm">

                    {user && (
                        <span className="text-slate-300">
              {user.email} — {user.role}
            </span>
                    )}

                    <button
                        onClick={handleLogout}
                        className="px-3 py-1 rounded bg-red-600 hover:bg-red-700"
                    >
                        Déconnexion
                    </button>
                </div>
            </div>
        </header>
    );
}
