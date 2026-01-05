import { useEffect, useState } from "react";
import {
    Box,
    Typography,
    Divider,
    CircularProgress,
    Alert,
    Stack,
} from "@mui/material";
import {useNavigate} from "react-router-dom";
import api from "../../api/axios"
import type {GameSessionTypePreview} from "../../types/GameSessionType.ts";
import GameSessionPreview from "../../components/GameSessionPreview.tsx";
import AddElementCard from "../../components/AddElementCard.tsx";
import { RefreshContext } from "../../components/RefreshContext.tsx";

export default function GameSessionList() {
    const [sessions, setSessions] = useState<GameSessionTypePreview[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const fetchGameSessions = async () => {
        try {
            setLoading(true);
            const { data } = await api.get<GameSessionTypePreview[]>('/sessions');
            setSessions(data);
        } catch {
            setError("Nie udało się pobrać listy gier.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGameSessions().then();
    }, []);

    return (
        <RefreshContext.Provider value={{ refresh: fetchGameSessions }}>
            <Box sx={{p: 3}}>
                <Typography variant="h4" gutterBottom>
                    Sesje gier
                </Typography>
                <Divider />
            </Box>

            <Box sx={{ p: 3 }}>
                {loading && (
                    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
                        <CircularProgress />
                    </Box>
                )}

                {error && (
                    <Alert severity="error">
                        {error}
                    </Alert>
                )}

                {!loading && !error && (
                    <Stack spacing={3}>
                        <AddElementCard title={"Dodaj nową sesję"}
                                        onClick={() => navigate("/sessions/new")}/>

                        {sessions.map((session) => (
                            <GameSessionPreview key={session.id} session={session}/>
                        ))}
                    </Stack>
                )}
            </Box>
        </RefreshContext.Provider>
    );
}
