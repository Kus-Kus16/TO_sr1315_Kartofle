import { createContext } from "react";

interface AuthContextType {
  username: string | null;
  setUsername: (name: string) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | null>(null);