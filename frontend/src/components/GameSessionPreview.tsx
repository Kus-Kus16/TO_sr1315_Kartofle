import {
    Card,
    CardActionArea,
    CardContent,
    Typography,
    Box,
    Stack,
    CardActions,
    Alert
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import type { GameSessionTypePreview} from "../types/GameSessionType.ts";
import {useState} from "react";
import GameSessionActionButtons from "./GameSessionActionButtons.tsx";

export default function GameSessionPreview({ session }: { session: GameSessionTypePreview }) {
    const [error, setError] = useState("");

    const isPastSession = new Date(session.date) < new Date();

    return (
        <Card
            sx={{ display: "flex", width: "100%", minHeight: 100, mb: 2 }}
        >
            <CardActionArea
                component={RouterLink}
                to={`/sessions/${session.id}`}
                sx={{ display: "flex", alignItems: "stretch", flex: 1 }}
            >

                <CardContent sx={{ flex: 1 }}>
                    <Stack spacing={1}>
                        <Typography variant="h5">
                            {session.title}
                        </Typography>
                        {session.selectedBoardGame && (
                            <Typography variant="h6">
                                {session.selectedBoardGame.title}
                            </Typography>
                        )}
                        <Typography variant="subtitle1" color="text.secondary">
                            {new Date(session.date).toLocaleDateString()}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Pozostałe miejsca: {session.numberOfPlayers - session.participants.length}
                        </Typography>
                    </Stack>
                </CardContent>

                <Stack spacing={2}
                    sx={{
                        width: 300,
                        p: 2,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                    }}
                >
                    {isPastSession && (
                        <Alert severity="warning" sx={{ width: "85%" }}>
                            Sesja już się odbyła
                        </Alert>
                    )}

                    {session.selectedBoardGame ? (
                        <Alert severity="success" sx={{ width: "85%" }} >
                            Gra wybrana
                        </Alert>
                    ) : (
                        <Alert severity="info" sx={{ width: "85%" }}>
                            Głosowanie aktywne
                        </Alert>
                    )}

                    {error && (
                        <Alert severity="error" sx={{ width: "85%" }}>
                            {error}
                        </Alert>
                    )}
                </Stack>
            </CardActionArea>
            <CardActions>
                <Box display="flex" alignItems="center" p={2}>
                    <GameSessionActionButtons session={session} setError={setError}/>
                </Box>
            </CardActions>
        </Card>
    );
};
