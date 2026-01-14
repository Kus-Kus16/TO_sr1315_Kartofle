import { useEffect, useState } from "react";
import { Grid, Box, Typography, Divider, CircularProgress, Alert } from "@mui/material";
import BoardGamePreview from "../../components/BoardGamePreview.tsx";
import type { BoardGameTypeDetails } from "../../types/BoardGameType.ts";
import {useNavigate} from "react-router-dom";
import api from "../../api/axios"
import AddElementCard from "../../components/AddElementCard.tsx";

export default function BoardGameList() {
    const [boardGames, setBoardGames] = useState<BoardGameTypeDetails[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchBoardGames = async () => {
            try {
                const { data } = await api.get<BoardGameTypeDetails[]>('/boardgames');
                setBoardGames(data);
            } catch {
                setError("Nie udało się pobrać listy gier.");
            } finally {
                setLoading(false);
            }
        };

        fetchBoardGames().then();
    }, []);
    
    return (
        <Box>
            <Box sx={{p: 3}}>
                <Typography variant="h4" gutterBottom>
                    Gry planszowe
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
                    <Grid container spacing={3} columns={16}>
                        <Grid size={{ xs: 12, sm: 6, md: 4 }}>
                            <AddElementCard title={"Dodaj nową grę"}
                                            onClick={() => navigate("/boardgames/new")}/>
                        </Grid>

                        {boardGames.map((game) => (
                            <Grid key={game.id} size={{ xs: 12, sm: 6, md: 4 }}>
                                <BoardGamePreview boardGame={game} showActions={true} />
                            </Grid>
                        ))}
                    </Grid>
                )}
            </Box>
        </Box>
    );
}
