import { useParams } from "react-router-dom";
import type { BoardGameType } from "../../types/boardgame";
import { useEffect, useState } from "react";
import AddGameSession from "../GameSessions/AddGameSession";

export default function BoardGameInsight() {

    const { id } = useParams<{ id: string }>();

    const [boardGame, setBoardGame] = useState<BoardGameType | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchBoardGame = async () => {
            try {
                const response = await fetch(`http://localhost:8080/boardgames/${id}`);
                if (!response.ok) throw new Error('Failed to fetch');
                const data = await response.json();
                setBoardGame(data);
            } catch (err) {
                setError("Something went wrong");
                console.log(err); 
            }
        };

        fetchBoardGame();
    }, [id]);

    return (
        <>
            <AddGameSession boardGameId={parseInt(id!)} />
            <div>
                {boardGame === null ? <p>{error}</p> : 
                <div>
                    <h1>{boardGame.title}</h1>
                    <p>Players: {boardGame.minPlayers} - {boardGame.maxPlayers}</p>
                    <p>Time: {boardGame.minutesPlaytime} minutes</p>
                    <p>Description: {boardGame.description}</p>
                </div>
                }
            </div>
        </>
    )
}
