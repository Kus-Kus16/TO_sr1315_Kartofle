import { useEffect, useState } from "react";
import type { GameSessionType } from "../../types/gamesession";
import GameSessionPreview from "./GameSessionPreview";

export default function GameSesions() {

  const [gameSessions, setGameSessions] = useState<GameSessionType[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {

    const loadGameSessions = async () => {
      try {
        const response = await fetch('http://localhost:8080/sessions');
        if (!response.ok) throw new Error("Something went wrong");
        const data = await response.json();
        setGameSessions(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load game sessions");
        console.error(err);
      }
    };

    loadGameSessions();    
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <>
      {error ? (<div>{error}</div>) : 
        (<div>
          {gameSessions.map((session) => (
            <GameSessionPreview
              key={session.id}
              session={session}
            />
          ))}
        </div>)
      }
    </>
  )
  }
