import React, {useState} from "react";
import type {ExternalBoardGameTypeEntry} from "../types/BoardGameType.ts";
import api from "../api/axios.tsx";
import {useNavigate} from "react-router-dom";
import {Avatar, Box, Button, CircularProgress, List, ListItem, ListItemAvatar,
    ListItemText, Stack, TextField, Typography } from "@mui/material";
import CasinoIcon from '@mui/icons-material/Casino';

interface BoardGameImportFormProps {
    setError: (error: string) => void;
}

export default function BoardGameImportForm({setError}: BoardGameImportFormProps) {
    const navigate = useNavigate();

    const [query, setQuery] = useState("");
    const [results, setResults] = useState<ExternalBoardGameTypeEntry[]>([]);
    const [loading, setLoading] = useState(false);
    const [isFetched, setIsFetched] = useState(false);

    const searchGames = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!query.trim()) {
            return;
        }

        setLoading(true);
        setIsFetched(false);
        setError("");

        try {
            const { data } = await api.get<ExternalBoardGameTypeEntry[]>(`/boardgames/external`, {
                params: {
                    query: query
                }
            });
            setResults(data);
            setIsFetched(true);
        } catch {
            setError("Nie udało się pobrać wyników");
        } finally {
            setLoading(false);
        }
    };

    const addGame = async (gameId: number) => {
        try {
            setError("");
            await api.post(`/boardgames/external/${gameId}`);
            navigate("/boardgames");
        } catch {
            setError("Nie udało się dodać gry");
        }
    };

    return (
        <Box maxWidth={600}>
            <Stack direction="row" justifyContent="space-between" alignItems="center" width="100%" mb={2}>
                <Typography variant="h5">
                    Wyszukaj grę
                </Typography>

                <Box
                    component="a"
                    href="https://boardgamegeek.com/"
                    target="_blank"
                    rel="noopener noreferrer"
                    sx={{ display: "inline-block" }}
                >
                    <Box
                        component="img"
                        src="/images/powered_by_BGG.png"
                        alt="BGG"
                        sx={{
                            maxWidth: 120,
                            height: "auto"
                        }}
                    />
                </Box>

            </Stack>

            <Box component="form" onSubmit={searchGames} mb={2}>
                <Stack direction="row" spacing={2} alignItems={"center"}>
                    <TextField
                        label="Nazwa gry"
                        value={query}
                        onChange={(e: { target: { value: React.SetStateAction<string>; }; }) => setQuery(e.target.value)}
                        fullWidth
                    />
                    <Button
                        type="submit"
                        variant="contained"
                        disabled={loading}
                        sx={{
                            width:'150px',
                            height:'50px',
                        }}
                    >
                        Szukaj
                    </Button>
                </Stack>
            </Box>

            {loading && (
                <Box display="flex" justifyContent="center" my={2}>
                    <CircularProgress />
                </Box>
            )}

            <List>
                {results.map((game) => (
                    <ListItem
                        key={game.id}
                        secondaryAction={
                            <Button
                                variant="outlined"
                                onClick={() => addGame(game.id)}
                            >
                                Dodaj
                            </Button>
                        }
                    >
                        <ListItemAvatar>
                            <Avatar variant="rounded">
                                <CasinoIcon/>
                            </Avatar>
                        </ListItemAvatar>

                        <ListItemText
                            primary={game.name}
                            secondary={game.releaseYear}
                        />
                    </ListItem>
                ))}
            </List>

            {isFetched && results.length === 0 && (
                <Box textAlign="center" mt={2}>
                    <Typography variant="body1" color="text.secondary">
                        Brak wyników
                    </Typography>
                </Box>
            )}
        </Box>
    );
}