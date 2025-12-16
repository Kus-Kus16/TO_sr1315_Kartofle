import { useEffect, useState } from "react";
import type { GameSessionType } from "../../types/gamesession";
import GameSession from "./GameSession";
import type { BoardGame } from "../../types/boardgame";

const mockBoardGames: BoardGame[] = [
  {
    id: 1,
    title: "Catan",
    description: "Trade, build and settle the island of Catan.",
    minPlayers: 3,
    maxPlayers: 4,
    minutesPlaytime: 90,
  },
  {
    id: 2,
    title: "Chess",
    description: "Classic abstract strategy game.",
    minPlayers: 2,
    maxPlayers: 2,
    minutesPlaytime: 60,
  },
];

export default function GameSesions() {

  const [gameSessions, setGameSessions] = useState<GameSessionType[]>([]);
  const [error, setError] = useState<string | null>(null);

  const setGameMockSessions = async () => {
    const mockGameSessions: GameSessionType[] = [
      {
        id: 1,
        date: "2025-01-01",
        playercount: 4,
        description: "Mock session for testing",
        owner: "MockOwner",
        boardgame: mockBoardGames[0]
      },
      {
        id: 2,
        date: "2025-01-02",
        playercount: 2,
        description: "Another mock session",
        owner: "TestUser",
        boardgame: mockBoardGames[1]
      },
];
    setGameSessions(mockGameSessions);
  }

  useEffect(() => {

    const loadGameSessions = async () => {
      try {
        const response = await fetch('http://localhost:8080/sessions');
        if (!response.ok) throw new Error("Something went wrong");
        const data = await response.json();
        setGameSessions(data);
      } catch (err) {
        setGameMockSessions();

        // setError("Failed to load game sessions");
        console.error(err);
      }
    };

    loadGameSessions();
  }, []);

  return (
    <>
      {error ? (<div>{error}</div>) : 
        (<div>
          {gameSessions.map((session) => (
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
