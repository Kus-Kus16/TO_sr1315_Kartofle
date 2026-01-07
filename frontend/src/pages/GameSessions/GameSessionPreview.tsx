import { Link } from 'react-router'
import type { GameSessionType } from '../../types/gamesession'

export default function GameSessionPreview({ session }: { session: GameSessionType }) { 

    return (
        <Link to={`/sessions/${session.id}`} style={{textDecoration: "none"}}>
            <div style={{ border: "1px solid black" }}>
                <h3>{session.boardGame.title}</h3>
                <p>Sesja dla: {session.numberOfPlayers} graczy</p>
                <p>Opis: {session.description}</p>
            </div>
        </Link>
    )
}
