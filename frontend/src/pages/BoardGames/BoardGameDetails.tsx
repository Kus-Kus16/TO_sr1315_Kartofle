import {useContext, useEffect, useState} from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Box, Typography, Card, CardContent, CardActions, Button, CircularProgress, Alert, Stack, Tooltip } from "@mui/material";
import type {BoardGameTypeDetails} from "../../types/BoardGameType.ts";
import api, {baseURL} from "../../api/axios";
import {Link as RouterLink} from "react-router";
import {AuthContext} from "../../auth/AuthContext.tsx";
import ConfirmDialog from "../../components/ConfirmDialog.tsx";
import WideLayoutBox from "../../layout/WideLayoutBox.tsx";
import ImageMedia from "../../components/ImageMedia.tsx";

export default function BoardGameDetails() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const auth = useContext(AuthContext);

    const [boardGame, setBoardGame] = useState<BoardGameTypeDetails | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false)

    const handleDelete = async () => {
        if (!boardGame) {
            return;
        }

        try {
            await api.delete(`/boardgames/${boardGame.id}`);
            navigate("/boardgames");
        } catch {
            setError("Nie udało się usunąć gry.");
        }
    };

    useEffect(() => {
        const fetchBoardGame = async () => {
            try {
                const { data } = await api.get<BoardGameTypeDetails>(`/boardgames/${id}`);
                setBoardGame(data);
            } catch {
                setError("Nie udało się pobrać danych gry.");
            } finally {
                setLoading(false);
            }
        };

        fetchBoardGame().then();
    }, [id]);

    if (!boardGame && error) {
        return <Alert severity="error">{error}</Alert>;
    }

    return (
        <WideLayoutBox>
            {loading && (
                <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
                    <CircularProgress />
                </Box>
            )}

            {boardGame && (
                <>
                <Card>
                    <Stack direction="row" spacing={2}>
                        <ImageMedia displayImage={!!boardGame.imageUrl} imageUrl={`${baseURL}${boardGame.imageUrl}`}
                                    altText={boardGame.title} width={"75%"} maxHeight={"500px"}
                        />

                        <Box width="100%" display="flex" flexDirection="column" justifyContent="space-evenly" pl={2}>
                            <CardContent sx={{p: 3}}>
                                {error && (
                                    <Alert severity="error" sx={{mb: 2}}>
                                        {error}
                                    </Alert>
                                )}
                                <Typography variant="h4" gutterBottom>
                                    {boardGame.title}
                                </Typography>
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

                            <CardActions sx={{p: 3}}>
                                {!boardGame.discontinued ? (
                                    <Stack direction="row" spacing={2}>
                                        <Tooltip title={!auth.isAuthenticated ? "Musisz być zalogowany, aby stworzyć sesję" : ""}>
                                            <Box display="inline-flex">
                                                <Button
                                                    size="small"
                                                    variant="contained"
                                                    component={RouterLink}
                                                    to={`/sessions/new?boardGameId=${boardGame.id}`}
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
                                                    onClick={() => navigate(`/boardgames/${boardGame.id}/edit`)}
                                                >
                                                    Edytuj
                                                </Button>
                                                <Button
                                                    size="medium"
                                                    color="error"
                                                    onClick={() => setOpenDeleteDialog(true)}
                                                >
                                                    Usuń
                                                </Button>
                                            </>
                                        )}

                                        {boardGame.rulebookUrl && (
                                            <Button
                                                size="medium"
                                                color="info"
                                                href={`${baseURL}${boardGame.rulebookUrl}`}
                                            >
                                                Instrukcja
                                            </Button>
                                        )}
                                    </Stack>
                                ) : (
                                    <Alert severity="info">Gra nie jest już dostępna</Alert>
                                )}
                            </CardActions>
                        </Box>
                    </Stack>
                </Card>

                <ConfirmDialog
                    open={openDeleteDialog}
                    title="Usuń grę"
                    description="Czy na pewno chcesz usunąć tę grę? Tej operacji nie można cofnąć."
                    confirmText={"Usuń"}
                    onCancel={() => setOpenDeleteDialog(false)}
                    onConfirm={() => {
                        setOpenDeleteDialog(false)
                        handleDelete().then()
                    }}
                />
                </>
            )}
        </WideLayoutBox>
    );
}
