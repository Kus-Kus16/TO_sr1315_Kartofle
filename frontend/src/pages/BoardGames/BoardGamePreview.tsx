import { Card, CardContent, CardActions, Typography, Button, CardActionArea, CardMedia, Tooltip, Box } from "@mui/material";
import type { BoardGameType } from "../../types/BoardGameType.ts";
import { Link as RouterLink } from "react-router-dom";
import {useContext} from "react";
import {AuthContext} from "../../auth/AuthContext.tsx";

export default function BoardGamePreview({ boardGame }: { boardGame: BoardGameType }) {
    const auth = useContext(AuthContext);

    return (
        <Card sx={{ minWidth: 240, maxWidth: 345 }}>
            <CardActionArea component={RouterLink} to={`/boardgames/${boardGame.id}`}>
                <CardMedia
                    component="img"
                    height="140"
                    image="images/lorempicsum.jpg"
                    alt="boardgame image"
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
            <CardActions>
                <Tooltip title={auth.isAuthenticated ? "" : "Musisz być zalogowany, aby stworzyć sesję"}>
                    <Box display="inline-flex">
                        <Button
                            size="small"
                            color="primary"
                            component={RouterLink}
                            to="/sessions/add"
                            disabled={!auth.isAuthenticated}
                        >
                            Stwórz sesję
                        </Button>
                    </Box>
                </Tooltip>
            </CardActions>
        </Card>
    );
}
