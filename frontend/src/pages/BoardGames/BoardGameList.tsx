import { useEffect, useState } from "react";
import { Grid, Box, Typography, Divider, CircularProgress, Alert, Button, Stack, FormControl, Select, MenuItem, InputLabel } from "@mui/material";
import BoardGamePreview from "../../components/BoardGamePreview.tsx";
import type { BoardGameTypeDetails } from "../../types/BoardGameType.ts";
import {useNavigate, useSearchParams} from "react-router-dom";
import api from "../../api/axios"
import AddElementCard from "../../components/AddElementCard.tsx";

export default function BoardGameList() {
    const [boardGames, setBoardGames] = useState<BoardGameTypeDetails[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchParams, setSearchParams] = useSearchParams();
    const pageParam = Number(searchParams.get('page') ?? '0');
    const page = Number.isNaN(pageParam) ? 0 : pageParam;
    const sizeParam = Number(searchParams.get('size') ?? '20');
    const size = Number.isNaN(sizeParam) ? 20 : sizeParam;
    const [totalPages, setTotalPages] = useState<number | undefined>(undefined);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchBoardGames = async () => {
            setLoading(true);
            setError(null);
            try {
                const { data } = await api.get<{
                    content: BoardGameTypeDetails[];
                    totalPages: number;
                    totalElements: number;
                }>(`/boardgames/?page=${page}&size=${size}`);

                setBoardGames(data.content);
                setTotalPages(data.totalPages);
            } catch {
                setError("Nie udało się pobrać listy gier.");
            } finally {
                setLoading(false);
            }
        };

        fetchBoardGames().then();
    }, [page, size]);
    
    return (
        <Box>
            <Box sx={{m: 3}}>
                <Typography variant="h4" gutterBottom>
                    Gry planszowe
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
                    <Grid container spacing={3} columns={10}>
                        <Grid size={{ xs: 10, sm: 5, md: 2 }}>
                            <AddElementCard title={"Dodaj nową grę"}
                                            onClick={() => navigate("/boardgames/new")}
                                            minWidth={200} maxWidth={345}
                            />
                        </Grid>

                        {boardGames.map((game) => (
                            <Grid key={game.id} size={{ xs: 10, sm: 5, md: 2 }}>
                                <BoardGamePreview boardGame={game} showActions={true} />
                            </Grid>
                        ))}
                    </Grid>
                )}
                {!loading && !error && (
                    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
                        <Stack direction="row" spacing={2} alignItems="center">
                            <FormControl size="small" sx={{ minWidth: 100 }}>
                                <InputLabel id="size-select-label">Ilość</InputLabel>
                                <Select
                                    labelId="size-select-label"
                                    value={String(size)}
                                    label="Ilość"
                                    onChange={(e) => setSearchParams({ page: '0', size: String(e.target.value) })}
                                >
                                    <MenuItem value={1}>1</MenuItem>
                                    <MenuItem value={2}>2</MenuItem>
                                    <MenuItem value={10}>10</MenuItem>
                                    <MenuItem value={20}>20</MenuItem>
                                    <MenuItem value={50}>50</MenuItem>
                                </Select>
                            </FormControl>

                            <Button variant="outlined" onClick={() => setSearchParams({ page: String(Math.max   (0, page - 1)), size: String(size) })} disabled={page === 0}>
                                Poprzednia
                            </Button>
                            <Typography sx={{ alignSelf: 'center' }}>Strona {page + 1}</Typography>
                            <Button variant="outlined" onClick={() => setSearchParams({ page: String(page + 1), size: String(size) })} disabled={totalPages !== undefined ? page + 1 >= totalPages : boardGames.length < size}>
                                Następna
                            </Button>
                        </Stack>
                    </Box>
                )}
            </Box>
        </Box>
    );
}
