import type { VoteTypeDetails } from "../types/VoteType";
import type { GameSessionTypeDetails } from "../types/GameSessionType";
import {useContext, useState} from "react";
import {
    Button,
    Card,
    CardContent,
    Chip,
    Stack,
    Typography,
    Checkbox,
    FormControlLabel,
    Select,
    MenuItem,
    CardActionArea,
    CardActions, Alert,
    FormHelperText,
    Box,
} from "@mui/material";
import api from "../api/axios.tsx";
import {Link as RouterLink} from "react-router";
import {RefreshContext} from "./RefreshContext.tsx";

type VotingSectionProps = {
    session: GameSessionTypeDetails;
    currentUserId: number | undefined;
    isOwner: boolean;
    disable: boolean;
};

export default function VotingSection({ session, currentUserId, isOwner, disable }: VotingSectionProps) {
    const [votes, setVotes] = useState<VoteTypeDetails[]>(session.votes);
    const [voteSnapshot, setVoteSnapshot] = useState<VoteTypeDetails[]>(votes);
    const [sortBy, setSortBy] = useState<"likes" | "known">("likes");
    const [error, setError] = useState("");

    const refresh = useContext(RefreshContext)

    const getVotesForGame = (gameId: number) =>
        votes.filter((v) => v.boardGameId === gameId);

    const countVotes = (votes: VoteTypeDetails[]) => ({
        likes: votes.filter((v) => v.likes).length,
        known: votes.filter((v) => v.knows).length,
    });

    const handleVoteChange = async (boardGameId: number, type: "like" | "known", checked: boolean) => {
        if (currentUserId === undefined) {
            return;
        }

        const previousVotes = [...votes];

        setVotes((prev) => {
            const existing = prev.find(
                (v) => v.userId === currentUserId && v.boardGameId === boardGameId
            );

            if (existing) {
                return prev.map((v) =>
                    v.userId === currentUserId && v.boardGameId === boardGameId
                        ? {
                            ...v,
                            likes: type === "like" ? checked : v.likes,
                            knows: type === "known" ? checked : v.knows,
                        }
                        : v
                );
            } else {
                return [
                    ...prev,
                    {
                        userId: currentUserId,
                        boardGameId: boardGameId,
                        likes: type === "like" ? checked : false,
                        knows: type === "known" ? checked : false,
                    },
                ];
            }
        });

        try {
            const userVote =
                votes.find(
                    (v) => v.userId === currentUserId && v.boardGameId === boardGameId
                ) || { likes: false, knows: false };

            await api.patch(`/sessions/${session.id}/votes`, {
                boardGameId: boardGameId,
                likes: type === "like" ? checked : userVote.likes,
                knows: type === "known" ? checked : userVote.knows
            });

            setError("");
        } catch {
            setError("Błąd podczas głosowania");
            setVotes(previousVotes);
        }
    };

    const handleSelectBoardGame = async (boardGameId: number) => {
        try {
            await api.post(`/sessions/${session.id}/votes`, null, {
                params: {
                    selectedBoardGameId: boardGameId,
                }
            });
            setError("");
            refresh.refresh()
        } catch {
            setError("Błąd podczas wyboru gry")
        }
    }

    const sortedGames = [...session.boardGames].sort((a, b) => {
        const aVotes = countVotes(voteSnapshot.filter(v => v.boardGameId === a.id));
        const bVotes = countVotes(voteSnapshot.filter(v => v.boardGameId === b.id));

        return sortBy === "likes"
            ? bVotes.likes - aVotes.likes
            : bVotes.known - aVotes.known;
    });

    return (
        <Stack spacing={2}>
            <Stack direction="row" justifyContent="space-between">
                <Typography variant="h5">Głosowanie</Typography>

                <Box display="flex" flexDirection="column" alignItems="flex-end">
                    <Select
                        value={sortBy}
                        onChange={(e) => {
                            const newSort = e.target.value as "likes" | "known";
                            setSortBy(newSort);
                            setVoteSnapshot(votes);
                        }}
                    >
                        <MenuItem value="likes">Najbardziej lubiane</MenuItem>
                        <MenuItem value="known">Najbardziej znane</MenuItem>
                    </Select>
                    <FormHelperText sx={{ textAlign: "right" }}>Sortuj według</FormHelperText>
                </Box>
            </Stack>

            {error && (
                <Alert severity="error" sx={{mb: 2}}>
                    {error}
                </Alert>
            )}

            {sortedGames.map((boardGame) => {
                const gameVotes = getVotesForGame(boardGame.id);
                const { likes, known } = countVotes(gameVotes);
                const myVote = gameVotes.find((v) => v.userId === currentUserId) || {
                    likes: false,
                    knows: false,
                };

                return (
                    <Card key={boardGame.id} variant="outlined">
                        <Stack direction="row" alignItems="center">
                            <CardActionArea sx={{ flexGrow: 1 }} component={RouterLink} to={`/boardgames/${boardGame.id}`} >
                                <CardContent sx={{p: 2}}>
                                    <Stack direction="row" justifyContent="space-between" alignItems="center">
                                        <Typography width={"30%"} variant="h6">
                                            {boardGame.title}
                                        </Typography>
                                        <Stack direction="row" spacing={2}>
                                            <Chip label={`${likes} osób chce zagrać`} color="success"/>
                                            <Chip label={`${known} osób zna`} color="secondary" />
                                        </Stack>

                                    </Stack>
                                </CardContent>
                            </CardActionArea>
                            {!disable && (
                                <CardActions sx={{p: 2, flexShrink: 0}}>
                                    {isOwner ? (
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={() => handleSelectBoardGame(boardGame.id)}
                                        >
                                            Wybierz tę grę
                                        </Button>
                                    ) : (
                                        <Stack direction="row">
                                            <FormControlLabel
                                                control={
                                                    <Checkbox
                                                        checked={myVote.likes}
                                                        onChange={(e) =>
                                                            handleVoteChange(boardGame.id, "like", e.target.checked)}
                                                    />
                                                }
                                                label="Chcę zagrać"
                                            />
                                            <FormControlLabel
                                                control={
                                                    <Checkbox
                                                        checked={myVote.knows}
                                                        onChange={(e) =>
                                                            handleVoteChange(boardGame.id, "known", e.target.checked)}
                                                    />
                                                }
                                                label="Znam tę grę"
                                            />
                                        </Stack>
                                    )}
                                </CardActions>
                            )}
                        </Stack>
                    </Card>
                );
            })}
        </Stack>
    );
}