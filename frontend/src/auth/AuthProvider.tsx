import React, { useState } from "react";
import { AuthContext } from "./AuthContext";

export default function AuthProvider({ children }: { children: React.ReactNode }) {
    const [username, setUsername] = useState<string | null>(() => {
        return localStorage.getItem("username");
    });

    const login = (name: string) => {
        setUsername(name);
        localStorage.setItem("username", name);
    };

    const logout = () => {
        setUsername(null);
        localStorage.removeItem("username");
    };

    return (
        <AuthContext.Provider
            value={{
                username,
                login,
                logout,
                isAuthenticated: !!username,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

