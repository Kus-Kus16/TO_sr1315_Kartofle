import { useState } from "react";
import { AuthContext } from "./AuthContext";

export default function AuthProvider({ children }: { children: React.ReactNode }) {
  const [username, setUsername] = useState<string | null>(null);

  return (
    <AuthContext.Provider value={{ username, setUsername, logout: () => setUsername(null) }}>
      {children}
    </AuthContext.Provider>
  );
}
