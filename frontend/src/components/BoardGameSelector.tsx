import React, {useEffect, useState } from "react";
import type {BoardGameTypePreview} from "../types/BoardGameType.ts";
import api from "../api/axios.tsx";
import { TextField, Autocomplete, Stack, Chip } from "@mui/material";

interface BoardGameSelectorProps {
    selectedBoardGames: BoardGameTypePreview[];
    setSelectedBoardGames: React.Dispatch<React.SetStateAction<BoardGameTypePreview[]>>;
    setMinPlayersGame: React.Dispatch<React.SetStateAction<BoardGameTypePreview | undefined>>;
    setMaxPlayersGame: React.Dispatch<React.SetStateAction<BoardGameTypePreview | undefined>>;
    fieldErrors?: {
        games?: string;
    };
    initialBoardGameId?: number;
}

export default function BoardGameSelector({ selectedBoardGames, setSelectedBoardGames, setMinPlayersGame,
                                              setMaxPlayersGame, fieldErrors, initialBoardGameId }: BoardGameSelectorProps) {

    const [allBoardGames, setAllBoardGames] = useState<BoardGameTypePreview[]>([]);
    const [inputValue, setInputValue] = useState("");

    useEffect(() => {
        const fetchBoardGames = async () => {
            try {
                const res = await api.get<BoardGameTypePreview[]>("/boardgames/previews");
                setAllBoardGames(res.data);

                if (initialBoardGameId) {
                    const game = res.data.find(bg => bg.id === initialBoardGameId);

                    if (game) {
                        setSelectedBoardGames([game]);
                        setMinPlayersGame(game);
                        setMaxPlayersGame(game);
                    }
                }
            } catch (err) {
                console.error("Nie udało się pobrać listy gier", err);
            }
        };
        fetchBoardGames().then();
    }, [initialBoardGameId, setMaxPlayersGame, setMinPlayersGame, setSelectedBoardGames]);

    const availableGames = allBoardGames.filter(bg => !selectedBoardGames.some(s => s.id === bg.id));

    const addGame = (game: BoardGameTypePreview) => {
        if (!selectedBoardGames.some(bg => bg.id === game.id)) {
            setSelectedBoardGames(prev => [...prev, game]);
            setMinPlayersGame(prev => !prev ? game : (game.minPlayers > prev.minPlayers ? game : prev));
            setMaxPlayersGame(prev => !prev ? game : (game.maxPlayers < prev.maxPlayers ? game : prev));
        }
        setInputValue("");
    };

    const removeGame = (game: BoardGameTypePreview) => {
        setSelectedBoardGames(prev => {
            const updated = prev.filter(bg => bg.id !== game.id);

            if (updated.length === 0) {
                setMinPlayersGame(undefined);
                setMaxPlayersGame(undefined);
            } else {
                setMinPlayersGame(updated.reduce((acc, g) => g.minPlayers > acc.minPlayers ? g : acc, updated[0]));
                setMaxPlayersGame(updated.reduce((acc, g) => g.maxPlayers < acc.maxPlayers ? g : acc, updated[0]));
            }

            return updated;
        });
    };

    const removeAllGames = () => {
        setSelectedBoardGames([]);
        setMinPlayersGame(undefined);
        setMaxPlayersGame(undefined);
    };

    return (
        <>
            <Autocomplete
                options={availableGames}
                getOptionLabel={(option) => option.title}
                inputValue={inputValue}
                onInputChange={(_, newValue) => setInputValue(newValue)}
                onChange={(_, value) => value && addGame(value)}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        placeholder="Wyszukaj grę"
                        error={!!fieldErrors?.games}
                        helperText={fieldErrors?.games}
                    />
                )}
                disableClearable
                disableCloseOnSelect
                openOnFocus
                autoComplete
            />

            <Stack direction="row" gap={1} flexWrap="wrap" mt={1}>
                {selectedBoardGames.map(game => (
                    <Chip
                        key={game.id}
                        label={game.title}
                        onDelete={() => removeGame(game)}
                        color="secondary"
                        variant="outlined"
                        sx={{ mb: 1 }}
                    />
                ))}

                {selectedBoardGames.length > 0 && (
                    <Chip
                        label="Wyczyść"
                        onClick={removeAllGames}
                        color="error"
                        variant="filled"
                        sx={{ mb: 1 }}
                    />
                )}
            </Stack>
        </>
    );
}
