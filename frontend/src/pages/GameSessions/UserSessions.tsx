import { useContext, useEffect, useState } from "react";
import type { GameSessionType } from "../../types/gamesession";
import GameSession from "./GameSession";
import { AuthContext } from "../../context/AuthContext";

export default function GameSesions() {

  const auth = useContext(AuthContext);

  const [gameSessions, setGameSessions] = useState<GameSessionType[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {

    const loadGameSessions = async (username: string) => {
        try {
            const response = await fetch("http://localhost:8080/userSessions", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username }),
            });

            if (!response.ok) {
            throw new Error("Something went wrong");
            }

            const data = await response.json();
            setGameSessions(data);
        } catch (err) {
            setError("Failed to load user game sessions");
            console.error(err);
        }
    };

    console.log("Auth username:", auth?.username);

    if (auth?.username) {
        loadGameSessions(auth.username);
    }
    else {        
        setError("User not authenticated");
    }
  }, [auth]);

  return (
    <>
        {error ? (<div>{error}</div>) : 
            (<div>
                {gameSessions
                    .filter((session) => session.owner === auth?.username)
                    .map((session) => (
                        <GameSession
                            key={session.id}
                            session={session}
                        />
                    ))}
            </div>)
        }
    </>
)
  }
