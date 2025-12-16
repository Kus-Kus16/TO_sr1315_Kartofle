import { useContext } from 'react';
import { AuthContext } from '../../context/AuthContext';
import type { GameSessionType } from '../../types/gamesession'

export default function GameSession({ session }: { session: GameSessionType }) {
    const auth = useContext(AuthContext);

    const handleJoinSession = async () => {
        try {
            const formData = { username: auth?.username };

            const response = await fetch(`http://localhost:8080/session/${session.id}/join`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            })

            if (!response.ok) throw new Error('Failed to join session')

            alert('Session joined successfully!')
        } catch (err) {
            console.log(err);
            alert('An error occurred while joining the session.');
        }
    }

    return (
        <div className="board-game-preview" style={{ border: "1px solid black" }}>
            <h3>{session.boardgame.title}</h3>
            <p>Players: {session.boardgame.minPlayers} - {session.boardgame.maxPlayers}</p>
            <button onClick={handleJoinSession}>Join</button>
        </div>
    )
}
