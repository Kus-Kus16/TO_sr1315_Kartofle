import { useEffect, useState } from "react";
import {
    Box,
    Typography,
    Divider,
    CircularProgress,
    Alert,
    Stack,
    TextField,
    Button,
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
    const [filterUsername, setFilterUsername] = useState<string | null>(null);
    const [filterBoardGameName, setFilterBoardGameName] = useState<string | null>(null);
    const [filterMinutes, setFilterMinutes] = useState<number | null>(null);
    const [filterMinPlayers, setFilterMinPlayers] = useState<number | null>(null);
    const [filterMaxPlayers, setFilterMaxPlayers] = useState<number | null>(null);
    const navigate = useNavigate();

    const fetchGameSessions = async (filters?: {
        username?: string | null;
        boardGameName?: string | null;
        minutes?: number | null;
        minNumberOfPlayers?: number | null;
        maxNumberOfPlayers?: number | null;
    }) => {
        try {
            setLoading(true);

            const params: Record<string, unknown> = {};
            if (filters) {
                if (filters.username) params.username = filters.username;
                if (filters.boardGameName) params.boardGameName = filters.boardGameName;
                if (filters.minutes != null) params.minutes = filters.minutes;
                if (filters.minNumberOfPlayers != null) params.minNumberOfPlayers = filters.minNumberOfPlayers;
                if (filters.maxNumberOfPlayers != null) params.maxNumberOfPlayers = filters.maxNumberOfPlayers;
            }

            const { data } = await api.get<GameSessionTypePreview[]>('/sessions' + (Object.keys(params).length ? '/filter' : ''), { params });
            setSessions(data);
        } catch {
            setError("Nie udało się pobrać listy sesji.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGameSessions().then();
    }, []);

    return (
        <RefreshContext.Provider value={{ refresh: fetchGameSessions }}>
            <Box sx={{m: 3}}>
                <Typography variant="h4" gutterBottom>
                    Sesje gier
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
                    <Stack spacing={3}>
                        <Box>
                            <Stack direction="row" spacing={2} flexWrap="wrap" alignItems="center">
                                <Box sx={{ minWidth: 200 }}>
                                    <TextField fullWidth label="Użytkownik" value={filterUsername ?? ''} onChange={e => setFilterUsername(e.target.value || null)} />
                                </Box>
                                <Box sx={{ minWidth: 200 }}>
                                    <TextField fullWidth label="Nazwa gry" value={filterBoardGameName ?? ''} onChange={e => setFilterBoardGameName(e.target.value || null)} />
                                </Box>
                                <Box sx={{ minWidth: 120 }}>
                                    <TextField type="number" fullWidth label="Max min" value={filterMinutes ?? ''} onChange={e => setFilterMinutes(e.target.value ? Number(e.target.value) : null)} />
                                </Box>
                                <Box sx={{ minWidth: 120 }}>
                                    <TextField type="number" fullWidth label="Min graczy" value={filterMinPlayers ?? ''} onChange={e => setFilterMinPlayers(e.target.value ? Number(e.target.value) : null)} />
                                </Box>
                                <Box sx={{ minWidth: 120 }}>
                                    <TextField type="number" fullWidth label="Max graczy" value={filterMaxPlayers ?? ''} onChange={e => setFilterMaxPlayers(e.target.value ? Number(e.target.value) : null)} />
                                </Box>
                                <Box>
                                    <Button variant="contained" onClick={() => fetchGameSessions({
                                        username: filterUsername,
                                        boardGameName: filterBoardGameName,
                                        minutes: filterMinutes,
                                        minNumberOfPlayers: filterMinPlayers,
                                        maxNumberOfPlayers: filterMaxPlayers
                                    })}>Filtruj</Button>
                                </Box>
                                <Box>
                                    <Button variant="outlined" onClick={() => {
                                        setFilterUsername(null);
                                        setFilterBoardGameName(null);
                                        setFilterMinutes(null);
                                        setFilterMinPlayers(null);
                                        setFilterMaxPlayers(null);
                                        fetchGameSessions();
                                    }}>Wyczyść</Button>
                                </Box>
                            </Stack>
                        </Box>

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
