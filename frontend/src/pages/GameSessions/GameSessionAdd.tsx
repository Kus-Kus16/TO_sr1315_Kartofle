import {useNavigate, useSearchParams} from "react-router-dom";
import GameSessionForm from "../../components/GameSessionForm.tsx";
import type { GameSessionTypeCreate } from "../../types/GameSessionType.ts";
import api from "../../api/axios.tsx";

export default function GameSessionAdd() {
    const navigate = useNavigate();

    const [searchParams] = useSearchParams();
    const boardGameIdParam = searchParams.get("boardGameId");
    const initialBoardGameId = boardGameIdParam
        ? Number(boardGameIdParam)
        : undefined;

    const handleAdd = async (
        formData: GameSessionTypeCreate,
        setError: (msg: string) => void
    ) => {
        try {
            await api.post("/sessions", formData);
            navigate(`/sessions`);
        } catch {
            setError("Nie udało się dodać sesji.");
        }
    };

    return (
        <GameSessionForm
            formTitle="Dodaj nową sesję"
            onSubmit={handleAdd}
            initialBoardGameId={initialBoardGameId}
        />
    );
}
