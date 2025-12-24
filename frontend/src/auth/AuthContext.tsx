import { createContext } from "react";

interface AuthContextType {
    username: string | null;
    login: (username: string) => void;
    logout: () => void;
    isAuthenticated: boolean;
}

export const AuthContext = createContext<AuthContextType>(null!);
