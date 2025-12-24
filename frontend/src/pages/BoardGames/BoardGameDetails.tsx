import {useContext, useEffect, useState} from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
    Box,
    Typography,
    Card,
    CardMedia,
    CardContent,
    CardActions,
    Button,
    CircularProgress,
    Alert,
    Stack,
    Tooltip
} from "@mui/material";
import type { BoardGameType } from "../../types/BoardGameType.ts";
import api from "../../api/axios";
import {Link as RouterLink} from "react-router";
import {AuthContext} from "../../auth/AuthContext.tsx";

export default function BoardGameDetails() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [boardGame, setBoardGame] = useState<BoardGameType | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const auth = useContext(AuthContext);

    const handleDelete = async () => {
        if (!boardGame) {
            return;
        }
        if (!window.confirm("Czy na pewno chcesz usunąć tę grę?")) {
            return;
        }
        
        try {
            await api.delete(`/boardgames/${boardGame.id}`);
            navigate("/boardgames");
        } catch (err) {
            console.error(err);
            alert("Nie udało się usunąć gry.");
        }
    };

    useEffect(() => {
        const fetchBoardGame = async () => {
            try {
                const { data } = await api.get<BoardGameType>(`/boardgames/${id}`);
                setBoardGame(data);
            } catch (err) {
                console.error(err);
                setError("Nie udało się pobrać danych gry.");
            } finally {
                setLoading(false);
            }
        };

        fetchBoardGame().then();
    }, [id]);

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return <Alert severity="error">{error}</Alert>;
    }

    if (!boardGame) return null;

    return (
        <Box sx={{ p: 3, maxWidth: 700, margin: 'auto' }}>
            <Card>
                <CardMedia
                    component="img"
                    height="300"
                    image="images/lorempicsum.jpg"
                    alt={boardGame.title}
                />
                <CardContent>
                    <Typography variant="h4" gutterBottom>{boardGame.title}</Typography>
                    <Typography variant="body1" sx={{ color: 'text.secondary', mb: 1 }}>
                        Gracze: {boardGame.minPlayers} - {boardGame.maxPlayers}
                    </Typography>
                    <Typography variant="body1" sx={{ color: 'text.secondary', mb: 1 }}>
                        Czas gry: {boardGame.minutesPlaytime}'
                    </Typography>
                    <Typography variant="body2" sx={{ mt: 2 }}>
                        {boardGame.description}
                    </Typography>
                </CardContent>
                <CardActions>
                    <Stack direction="row" spacing={2}>
                        <Tooltip title={!auth.isAuthenticated ? "Musisz być zalogowany, aby stworzyć sesję" : ""}>
                            <Box display="inline-flex">
                                <Button
                                    size="small"
                                    variant="contained"
                                    component={RouterLink}
                                    to="/sessions/add"
                                    disabled={!auth.isAuthenticated}
                                >
                                    Stwórz sesję
                                </Button>
                            </Box>
                        </Tooltip>

                        {auth.isAuthenticated && (
                            <>
                            <Button
                                size="medium"
                                color="secondary"
                                onClick={() => navigate(`/boardgames/edit/${boardGame.id}`)}
                            >
                                Edytuj
                            </Button>
                            <Button
                            size="medium"
                            color="error"
                            onClick={handleDelete}
                            >
                                Usuń
                            </Button>
                            </>
                        )}

                        {!boardGame.rulebookUrl && (
                            <Button
                                size="medium"
                                color="info"
                                href={boardGame.rulebookUrl}
                            >
                                Pobierz instrukcję
                            </Button>
                        )}
                    </Stack>
                </CardActions>
            </Card>
        </Box>
    );
}
