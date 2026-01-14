import { Chip, Divider, Stack, Typography } from "@mui/material";
import type {GameSessionTypeDetails} from "../types/GameSessionType.ts";
import { StarBorder } from '@mui/icons-material';

export default function GameSessionInfo({ session }: { session: GameSessionTypeDetails }) {
    const isPastSession = new Date(session.date) < new Date();
    const isFull = session.participants.length >= session.numberOfPlayers;

    const hasSelectedGame = !!session.selectedBoardGameId;

    return (
        <Stack spacing={2}>
            <Typography variant="h4">{session.title}</Typography>

            <Typography color="text.secondary">
                {new Date(session.date).toLocaleDateString()}
            </Typography>

            <Stack direction="row" spacing={1}>
                {isPastSession && (
                    <Chip
                        color={"warning"}
                        label={"Sesja już się odbyła"}
                        sx={{ width: "fit-content" }}
                    />
                )}
                <Chip
                    color={hasSelectedGame ? "success" : "info"}
                    label={
                        hasSelectedGame
                            ? "Gra wybrana"
                            : "Głosowanie aktywne"
                    }
                    sx={{ width: "fit-content" }}
                />
                {isFull && (
                    <Chip
                        color={"error"}
                        label={"Sesja jest pełna"}
                        sx={{ width: "fit-content" }}
                    />
                )}
            </Stack>

            <Typography variant="body2" sx={{ mt: 2 }}>
                {session.description}
            </Typography>

            <Divider />

            <Typography variant="subtitle1">
                {"Uczestnicy " + session.participants.length + "/" + session.numberOfPlayers + ":"}
            </Typography>

            <Stack direction="row" spacing={1} flexWrap="wrap">
                {session.participants.map(p => (
                    <Chip key={p.id} label={p.username}
                          color={p.id == session.ownerId ? "secondary" : "primary"}
                          icon={p.id == session.ownerId ? <StarBorder/> : undefined}
                    />
                ))}
            </Stack>
        </Stack>
    );
}
