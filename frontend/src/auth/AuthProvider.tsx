import React, { useState } from "react";
import { AuthContext } from "./AuthContext";

export default function AuthProvider({ children }: { children: React.ReactNode }) {
    const [username, setUsername] = useState<string | null>(() => {
        return sessionStorage.getItem("username");
    });

    const login = (name: string) => {
        setUsername(name);
        sessionStorage.setItem("username", name);
    };

    const logout = () => {
        setUsername(null);
        sessionStorage.removeItem("username");
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

