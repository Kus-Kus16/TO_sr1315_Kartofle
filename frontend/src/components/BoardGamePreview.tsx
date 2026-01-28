import { Card, CardContent, CardActions, Typography, Button, CardActionArea, Tooltip, Box } from "@mui/material";
import type { BoardGameTypeDetails } from "../types/BoardGameType.ts";
import { Link as RouterLink } from "react-router-dom";
import {useContext} from "react";
import {AuthContext} from "../auth/AuthContext.tsx";
import {baseURL} from "../api/axios.tsx";
import ImageMedia from "./ImageMedia.tsx";

interface BoardGamePreviewProps {
    boardGame: BoardGameTypeDetails;
    showActions: boolean;
}

export default function BoardGamePreview({ boardGame, showActions }: BoardGamePreviewProps) {
    const auth = useContext(AuthContext);

    return (
        <Card sx={{ minWidth: 200, maxWidth: 345 }}>
            <CardActionArea component={RouterLink} to={`/boardgames/${boardGame.id}`} sx={{ height: "100%" }}>

                <ImageMedia displayImage={!!boardGame.imageUrl} imageUrl={`${baseURL}${boardGame.imageUrl}`}
                            altText={boardGame.title}
                />

                <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                        {boardGame.title}
                    </Typography>
                    <Typography variant="body1" sx={{ color: 'text.secondary' }}>
                        Gracze: {boardGame.minPlayers} - {boardGame.maxPlayers}
                    </Typography>
                    <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                        Czas gry: {boardGame.minutesPlaytime}'
                    </Typography>
                </CardContent>
            </CardActionArea>

            {showActions && (
                <CardActions>
                    <Tooltip title={auth.isAuthenticated ? "" : "Musisz być zalogowany, aby stworzyć sesję"}>
                        <Box display="inline-flex">
                            <Button
                                size="small"
                                color="primary"
                                component={RouterLink}
                                to={`/sessions/new?boardGameId=${boardGame.id}`}
                                disabled={!auth.isAuthenticated}
                            >
                                Stwórz sesję
                            </Button>
                        </Box>
                    </Tooltip>
                </CardActions>
            )}

        </Card>
    );
}
