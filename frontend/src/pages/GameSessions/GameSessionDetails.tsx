import {useContext, useEffect, useMemo, useState} from "react";
import { useParams } from "react-router-dom";
import { Box, Card, CardContent, CardActions, CircularProgress, Alert, Stack } from "@mui/material";
import api from "../../api/axios";
import type {GameSessionTypeDetails} from "../../types/GameSessionType.ts";
import GameSessionInfo from "../../components/GameSessionInfo.tsx";
import BoardGamePreview from "../../components/BoardGamePreview.tsx";
import GameSessionActionButtons from "../../components/GameSessionActionButtons.tsx";
import type {BoardGameTypeDetails} from "../../types/BoardGameType.ts";
import {RefreshContext} from "../../components/RefreshContext.tsx";
import VotingSection from "../../components/GameSessionVoting.tsx";
import {AuthContext} from "../../auth/AuthContext.tsx";

export default function GameSessionDetails() {
    const { id } = useParams<{ id: string }>();
    const [session, setSession] = useState<GameSessionTypeDetails | null>(null);
    const [boardGame, setBoardGame] = useState<BoardGameTypeDetails>();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const auth = useContext(AuthContext)

    const { isOwner, isDisabled, currentUserId } = useMemo(() => {
        if (!session) {
            return { isOwner: false, isDisabled: true, currentUserId: undefined };
        }

        const foundUser = session.participants.find((p) => p.username === auth.username);

        const participant = foundUser !== undefined;
        const pastSession = new Date(session.date) < new Date();
        const disabled = !participant || pastSession || session.selectedBoardGameId !== null;

        return {
            isOwner: session.ownerId === foundUser?.id,
            isDisabled: disabled,
            currentUserId: foundUser?.id ?? undefined,
        };
    }, [session, auth.username]);

    const fetchGameSession = async () => {
        try {
            setLoading(true);
            const { data } = await api.get<GameSessionTypeDetails>(`/sessions/${id}`);
            setSession(data);
            console.log(data)

            const selected =
                data.boardGames.find((game) => game.id === data.selectedBoardGameId)
                || undefined;
            setBoardGame(selected);
        } catch {
            setError("Nie udało się pobrać danych sesji.");
        } finally {
            setLoading(false);
        }
    };
    
    useEffect(() => {
        fetchGameSession().then();
    }, []);

    if (!session && error) {
        return <Alert severity="error">{error}</Alert>;
    }

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <RefreshContext.Provider value={{ refresh: fetchGameSession }}>
        {session && (
            <Stack direction="column" spacing={5} sx={{px: 15}}>
                <Card sx={{ p: 2 }}>
                    <CardContent>
                        <Stack direction="row" spacing={3} justifyContent="space-between" alignItems={boardGame ? "start" :"end"}>
                            <Stack sx={{ maxWidth: "60%" }} spacing={2}>
                                <GameSessionInfo session={session} />

                                {error && (
                                    <Alert severity="error" sx={{mb: 2}}>
                                        {error}
                                    </Alert>
                                )}
                            </Stack>

                            <Stack direction="column" spacing={3} >
                                {boardGame && (
                                    <Box sx={{ flexGrow: 1 }}>
                                        <BoardGamePreview boardGame={boardGame} showActions={false}/>
                                    </Box>
                                )}

                                <CardActions sx={{ justifyContent: "flex-end", p: 0}}>
                                    <GameSessionActionButtons session={{
                                        ...session,
                                        selectedBoardGame: boardGame
                                    }} setError={setError}/>
                                </CardActions>
                            </Stack>
                        </Stack>
                    </CardContent>
                </Card>

                {session.boardGames.length > 1 && (
                    <Card sx={{ p: 2 }}>
                        <CardContent>
                            <VotingSection session={session} currentUserId={currentUserId} isOwner={isOwner} disable={isDisabled}/>
                        </CardContent>
                    </Card>
                )}
            </Stack>
        )}
        </RefreshContext.Provider>
    );
}
