import { createContext, useContext, useState, useCallback } from "react";
import { login as loginApi } from "../api/authApi";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem("auth");
        return stored ? JSON.parse(stored) : null;
    });

    const login = useCallback(async (username, password) => {
        const data = await loginApi(username, password);
        const authData = {
            token: data.token,
            username: data.username,
            role: data.role,
            apartmentId: data.apartmentId,
        };
        localStorage.setItem("auth", JSON.stringify(authData));
        setUser(authData);
    }, []);

    const logout = useCallback(() => {
        localStorage.removeItem("auth");
        setUser(null);
    }, []);

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}