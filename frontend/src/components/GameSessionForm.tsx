import React, { useState } from "react";
import {
    Box,
    Card,
    CardContent,
    CardActions,
    TextField,
    Typography,
    Button,
    Stack,
    Alert,
    Divider
} from "@mui/material";
import type { GameSessionTypeCreate } from "../types/GameSessionType.ts";
import type { BoardGameTypeDetails } from "../types/BoardGameType.ts";
import { useNavigate } from "react-router-dom";
import BoardGameSelector from "./BoardGameSelector";

export interface GameSessionFormProps {
    initialData?: GameSessionTypeCreate;
    initialBoardGameId?: number;
    onSubmit: (
        formData: GameSessionTypeCreate,
        setError: (msg: string) => void
    ) => void;
    formTitle: string;
}

export default function GameSessionForm({ initialData, initialBoardGameId, onSubmit, formTitle }: GameSessionFormProps) {
    const [form, setForm] = useState<GameSessionTypeCreate>({
        title: initialData?.title || "",
        date: initialData?.date || "",
        numberOfPlayers: initialData?.numberOfPlayers || 1,
        description: initialData?.description || "",
        boardGamesIds: initialData?.boardGamesIds || []
    });

    const [error, setError] = useState<string | null>(null);
    const [fieldErrors, setFieldErrors] = useState<{
        title?: string;
        date?: string;
        numberOfPlayers?: string;
        games?: string;
    }>({});

    const [selectedBoardGames, setSelectedBoardGames] = useState<BoardGameTypeDetails[]>([]);
    const [minPlayersGame, setMinPlayersGame] = useState<BoardGameTypeDetails>();
    const [maxPlayersGame, setMaxPlayersGame] = useState<BoardGameTypeDetails>();

    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({
            ...prev,
            [name]: name === "numberOfPlayers" ? Number(value) : value
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const errors: typeof fieldErrors = {};

        if (!form.title.trim()) {
            errors.title = "Tytuł sesji jest wymagany";
        }

        if (!form.date) {
            errors.date = "Data jest wymagana";
        } else if (new Date(form.date) < new Date(new Date().toDateString())) {
            errors.date = "Data nie może być z przeszłości";
        }

        if (selectedBoardGames.length === 0) {
            errors.games = "Wybierz przynajmniej jedną grę do sesji";
        } else if (
            form.numberOfPlayers < (minPlayersGame?.minPlayers || 1) ||
            form.numberOfPlayers > (maxPlayersGame?.maxPlayers || 20)
        ) {
            errors.numberOfPlayers = `Liczba graczy musi być między ${minPlayersGame?.minPlayers || 1} a ${maxPlayersGame?.maxPlayers || 20}`;
        }

        setFieldErrors(errors);

        if (Object.keys(errors).length === 0) {
            const boardGamesIds = selectedBoardGames.map(bg => bg.id);
            onSubmit({ ...form, boardGamesIds }, setError);
        }
    };

    return (
        <Box sx={{ maxWidth: 700, margin: "auto" }}>
            <Card sx={{ p: 2 }}>
                <CardContent component="form" onSubmit={handleSubmit}>
                    <Stack spacing={2}>
                        <Typography variant="h4">{formTitle}</Typography>
                        {error && <Alert severity="error">{error}</Alert>}

                        <TextField
                            label="Tytuł sesji"
                            name="title"
                            value={form.title}
                            onChange={handleChange}
                            required
                            fullWidth
                            error={!!fieldErrors.title}
                            helperText={fieldErrors.title}
                        />

                        <Stack direction="row" spacing={2}>
                            <TextField
                                label="Data"
                                name="date"
                                type="date"
                                value={form.date}
                                onChange={handleChange}
                                slotProps={{ inputLabel: { shrink: true } }}
                                required
                                fullWidth
                                error={!!fieldErrors.date}
                                helperText={fieldErrors.date}
                            />

                            <TextField
                                label="Liczba graczy"
                                name="numberOfPlayers"
                                type="number"
                                value={form.numberOfPlayers}
                                onChange={handleChange}
                                slotProps={{
                                    input: {
                                        inputProps: {
                                            min: minPlayersGame?.minPlayers || 1,
                                            max: maxPlayersGame?.maxPlayers || 20
                                        }
                                    }
                                }}
                                required
                                fullWidth
                                error={!!fieldErrors.numberOfPlayers}
                                helperText={fieldErrors.numberOfPlayers}
                            />
                        </Stack>

                        <TextField
                            label="Opis"
                            name="description"
                            value={form.description}
                            onChange={handleChange}
                            multiline
                            rows={4}
                            fullWidth
                        />

                        <Divider />
                        <Typography variant="h6">Dodaj gry do sesji</Typography>
                        <BoardGameSelector
                            selectedBoardGames={selectedBoardGames}
                            setSelectedBoardGames={setSelectedBoardGames}
                            setMinPlayersGame={setMinPlayersGame}
                            setMaxPlayersGame={setMaxPlayersGame}
                            fieldErrors={fieldErrors}
                            initialBoardGameId={initialBoardGameId}
                        />
                    </Stack>

                    <CardActions sx={{ mt: 2, px: 0 }}>
                        <Stack direction="row" spacing={2}>
                            <Button type="submit" variant="contained">Zapisz</Button>
                            <Button color="error" variant="outlined" onClick={() => navigate(-1)}>Anuluj</Button>
                        </Stack>
                    </CardActions>
                </CardContent>
            </Card>
        </Box>
    );
}
