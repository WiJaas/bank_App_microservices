import { useAuth } from "../hooks/useAuth";

export default function RoleRoute({ allowedRole, children }) {
    const { role } = useAuth();

    if (role !== allowedRole) {
        return (
            <p style={{ color: "red", padding: "1rem" }}>
                Accès interdit : vous n’êtes pas autorisé à accéder à cette ressource.
            </p>
        );
    }

    return children;
}
