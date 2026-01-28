import {useContext, useEffect, useState} from "react";
import {
    Box,
    Typography,
    Divider,
    CircularProgress,
    Alert,
} from "@mui/material";
import api from "../../api/axios"
import type {GameSessionTypePreview} from "../../types/GameSessionType.ts";
import GameSessionPreview from "../../components/GameSessionPreview.tsx";
import {AuthContext} from "../../auth/AuthContext.tsx";
import { RefreshContext } from "../../components/RefreshContext.tsx";

export default function GameSessionListUser() {
    const auth = useContext(AuthContext);
    const [sessions, setSessions] = useState<GameSessionTypePreview[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchGameSessions = async () => {
        const fetch = async (username: string | null) => {
            try {
                const { data } = await api.get<GameSessionTypePreview[]>('/sessions', {
                    params: {
                        username: username,
                    }
                });
                setSessions(data);
            } catch {
                setError("Nie udało się pobrać listy gier.");
            } finally {
                setLoading(false);
            }
        };

        if (auth.isAuthenticated) {
            fetch(auth.username).then();
        } else {
            setError("Nie jesteś zalogowany");
        }
    }

    useEffect(() => {
        fetchGameSessions().then()
    }, [auth.isAuthenticated, auth.username]);

    return (
        <RefreshContext.Provider value={{ refresh: fetchGameSessions }}>
            <Box sx={{m: 3}}>
                <Typography variant="h4" gutterBottom>
                    Twoje sesje
                </Typography>
                <Divider />
            </Box>

            <Box sx={{ m: 3 }}>
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
                    <Box>
                        {sessions.map((session) => (
                            <GameSessionPreview key={session.id} session={session}/>
                        ))}
                    </Box>
                )}
            </Box>
        </RefreshContext.Provider>
    );
}
