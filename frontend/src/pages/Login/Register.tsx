import { useContext, useState } from "react";
import { AuthContext } from "../../context/AuthContext";

export default function Register() {
      const auth = useContext(AuthContext);
  
      const [login, setLogin] = useState<string>("");
      const [error, setError] = useState<string|null>(null);
  
      if (!auth) return null;
  
      const handleSubmit = async (e: React.FormEvent) => {
          e.preventDefault();
  
          const url:string = "http://localhost:8080/users";
          const payload = { username: login };
  
          try {
              const res = await fetch(url, {
                  method: "POST",
                  headers: {
                      "Content-Type": "application/json",
                  },
                  body: JSON.stringify(payload)
              });
  
              if (!res.ok) {
                  const msg = await res.text();
                  throw new Error(msg || "Register failed");
              }
  
              const data = await res.json();
              
              auth.setUsername(data.username);
              setError(`Registered successfully. Hi ${data.username}`);
          } catch (err) {
              setError("Register failed. Please try again.");
              console.error("Error:", err);
          }
          
      }   
  
  
      return (
          <div>
              <h2>Register</h2>
              <form onSubmit={handleSubmit}>
                  <label>Register</label>
                  <input
                      type="text"
                      required
                      value={login}
                      onChange={(e) => setLogin(e.target.value)}
                  />
  
                  <button type="submit">Register</button>
              </form>
  
              <div>{error && <p>{error}</p>}</div>
          </div>
      )
}
