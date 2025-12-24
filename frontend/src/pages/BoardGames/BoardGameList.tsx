import { useEffect, useState } from "react";
import { Grid, Box, Typography, Card, CardActionArea, CardContent, Divider, CircularProgress, Alert } from "@mui/material";
import BoardGamePreview from "./BoardGamePreview";
import type { BoardGameType } from "../../types/BoardGameType.ts";
import {useNavigate} from "react-router-dom";
import api from "../../api/axios"

export default function BoardGameList() {
    const [boardGames, setBoardGames] = useState<BoardGameType[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchBoardGames = async () => {
            try {
                const { data } = await api.get<BoardGameType[]>('/boardgames');
                setBoardGames(data);
            } catch (err) {
                console.error(err);
                setError("Nie udało się pobrać listy gier.");
            } finally {
                setLoading(false);
            }
        };

        fetchBoardGames().then();
    }, []);
    
    return (
        <Box sx={{ p: 1 }}>
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
                            <Card
                                sx={{
                                    minWidth: 240,
                                    height: "100%",
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    cursor: "pointer",
                                }}
                                onClick={() => navigate("/boardgames/add")}
                            >
                                <CardActionArea
                                    sx={{
                                        height: "100%",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center",
                                    }}
                                >
                                    <CardContent>
                                        <Typography variant="h3" align="center" color="primary">
                                            ＋
                                        </Typography>
                                        <Typography align="center">
                                            Dodaj nową grę
                                        </Typography>
                                    </CardContent>
                                </CardActionArea>
                            </Card>
                        </Grid>

                        {boardGames.map((game) => (
                            <Grid key={game.id} size={{ xs: 12, sm: 6, md: 4 }}>
                                <BoardGamePreview boardGame={game} />
                            </Grid>
                        ))}
                    </Grid>
                )}
            </Box>
        </Box>
    );
}
