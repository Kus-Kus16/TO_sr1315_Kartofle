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
    FormControl,
    Select,
    MenuItem,
} from "@mui/material";
import { useNavigate, useSearchParams } from "react-router-dom";
import api from "../../api/axios";
import type { GameSessionTypePreview } from "../../types/GameSessionType.ts";
import GameSessionPreview from "../../components/GameSessionPreview.tsx";
import AddElementCard from "../../components/AddElementCard.tsx";
import { RefreshContext } from "../../components/RefreshContext.tsx";

export default function GameSessionList() {
    const [sessions, setSessions] = useState<GameSessionTypePreview[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Filters
    const [filterUsername, setFilterUsername] = useState<string | null>(null);
    const [filterBoardGameName, setFilterBoardGameName] = useState<string | null>(null);
    const [filterMaxMinutesPlaytime, setFilterMaxMinutesPlaytime] = useState<number | null>(null);
    const [filterMinPlayers, setFilterMinPlayers] = useState<number | null>(null);
    const [filterMaxPlayers, setFilterMaxPlayers] = useState<number | null>(null);

    // Pagination
    const [searchParams, setSearchParams] = useSearchParams();
    const pageParam = Number(searchParams.get('page') ?? '0');
    const page = Number.isNaN(pageParam) ? 0 : pageParam;
    const sizeParam = Number(searchParams.get('size') ?? '20');
    const size = Number.isNaN(sizeParam) ? 20 : sizeParam;
    const [totalPages, setTotalPages] = useState<number | undefined>(undefined);

    const navigate = useNavigate();

    const fetchGameSessions = async (
        filters?: {
            username?: string | null;
            boardGameName?: string | null;
            maxMinutesPlaytime?: number | null;
            minNumberOfPlayers?: number | null;
            maxNumberOfPlayers?: number | null;
        },
        pageNumber: number = page,
        pageSize: number = size
    ) => {
        try {
            setLoading(true);
            setError(null);

            const params: Record<string, unknown> = {
                pageNumber,
                pageSize
            };

            if (filters) {
                if (filters.username) params.username = filters.username;
                if (filters.boardGameName) params.boardGameName = filters.boardGameName;
                if (filters.maxMinutesPlaytime != null) params.maxMinutesPlaytime = filters.maxMinutesPlaytime;
                if (filters.minNumberOfPlayers != null) params.minNumberOfPlayers = filters.minNumberOfPlayers;
                if (filters.maxNumberOfPlayers != null) params.maxNumberOfPlayers = filters.maxNumberOfPlayers;
            }

            const { data } = await api.get<{
                content: GameSessionTypePreview[];
                totalPages: number;
                totalElements: number;
            }>('/sessions/filterWithPage', { params });

            setSessions(data.content);
            setTotalPages(data.totalPages);
        } catch {
            setError("Nie udało się pobrać listy sesji.");
        } finally {
            setLoading(false);
        }
    };

    // Fetch sessions whenever page, size, or filters change
    useEffect(() => {
        fetchGameSessions({
            username: filterUsername,
            boardGameName: filterBoardGameName,
            maxMinutesPlaytime: filterMaxMinutesPlaytime,
            minNumberOfPlayers: filterMinPlayers,
            maxNumberOfPlayers: filterMaxPlayers
        }, page, size);
    }, [page, size]);

    const handleFilter = () => {
        setSearchParams({ page: '0', size: String(size) });
        fetchGameSessions({
            username: filterUsername,
            boardGameName: filterBoardGameName,
            maxMinutesPlaytime: filterMaxMinutesPlaytime,
            minNumberOfPlayers: filterMinPlayers,
            maxNumberOfPlayers: filterMaxPlayers
        }, 0, size);
    };

    const handleClearFilters = () => {
        setFilterUsername(null);
        setFilterBoardGameName(null);
        setFilterMaxMinutesPlaytime(null);
        setFilterMinPlayers(null);
        setFilterMaxPlayers(null);
        setSearchParams({ page: '0', size: String(size) });
        fetchGameSessions({}, 0, size);
    };

    return (
        <RefreshContext.Provider value={{ refresh: fetchGameSessions }}>
            <Box sx={{ m: 3 }}>
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
                    <Alert severity="error">{error}</Alert>
                )}

                {!loading && !error && (
                    <Stack spacing={3}>
                        {/* Filters */}
                        <Box>
                            <Stack direction="row" spacing={2} flexWrap="wrap" alignItems="center">
                                <TextField
                                    label="Użytkownik"
                                    value={filterUsername ?? ''}
                                    onChange={e => setFilterUsername(e.target.value || null)}
                                />
                                <TextField
                                    label="Nazwa gry"
                                    value={filterBoardGameName ?? ''}
                                    onChange={e => setFilterBoardGameName(e.target.value || null)}
                                />
                                <TextField
                                    type="number"
                                    label="Max min"
                                    value={filterMaxMinutesPlaytime ?? ''}
                                    onChange={e => setFilterMaxMinutesPlaytime(e.target.value ? Number(e.target.value) : null)}
                                />
                                <TextField
                                    type="number"
                                    label="Min graczy"
                                    value={filterMinPlayers ?? ''}
                                    onChange={e => setFilterMinPlayers(e.target.value ? Number(e.target.value) : null)}
                                />
                                <TextField
                                    type="number"
                                    label="Max graczy"
                                    value={filterMaxPlayers ?? ''}
                                    onChange={e => setFilterMaxPlayers(e.target.value ? Number(e.target.value) : null)}
                                />
                                <Button variant="contained" onClick={handleFilter}>Filtruj</Button>
                                <Button variant="outlined" onClick={handleClearFilters}>Wyczyść</Button>
                            </Stack>
                        </Box>

                        {/* Add new session */}
                        <AddElementCard
                            title="Dodaj nową sesję"
                            onClick={() => navigate("/sessions/new")}
                        />

                        {/* Session list */}
                        {sessions.map(session => (
                            <GameSessionPreview key={session.id} session={session} />
                        ))}

                        {/* Pagination */}
                        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
                            <Stack direction="row" spacing={2} alignItems="center">
                                <FormControl size="small">
                                    <Select
                                        value={String(size)}
                                        onChange={e => setSearchParams({ page: '0', size: e.target.value })}
                                    >
                                        {[1, 2, 10, 20, 50].map(s => (
                                            <MenuItem key={s} value={s}>{s}</MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>

                                <Button
                                    variant="outlined"
                                    onClick={() => setSearchParams({ page: String(Math.max(0, page - 1)), size: String(size) })}
                                    disabled={page === 0}
                                >
                                    Poprzednia
                                </Button>
                                <Typography sx={{ alignSelf: 'center' }}>Strona {page + 1}</Typography>
                                <Button
                                    variant="outlined"
                                    onClick={() => setSearchParams({ page: String(page + 1), size: String(size) })}
                                    disabled={totalPages !== undefined ? page + 1 >= totalPages : sessions.length < size}
                                >
                                    Następna
                                </Button>
                            </Stack>
                        </Box>
                    </Stack>
                )}
            </Box>
        </RefreshContext.Provider>
    );
}
