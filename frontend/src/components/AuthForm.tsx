import React, { useState } from "react";
import {Alert, Button, Paper, Stack, TextField, Typography} from "@mui/material";

interface AuthFormProps {
    title: string;
    buttonText: string;
    onSubmit: (username: string, setError: (msg: string) => void) => void;
}

export default function AuthForm({ title, buttonText, onSubmit }: AuthFormProps) {
    const [username, setUsername] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (!username.trim()) {
            setError("Podaj nazwę użytkownika");
            return;
        }

        setError("")
        onSubmit(username.trim(), setError);
    };

    return (
        <Paper sx={{ p: 5, width: 350 }}>
            <form onSubmit={handleSubmit}>
                <Stack spacing={2}>
                    <Typography variant="h5" gutterBottom align="center">
                        {title}
                    </Typography>

                    {error && <Alert severity="error">{error}</Alert>}

                    <TextField
                        label="Nazwa użytkownika"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        fullWidth
                    />

                    <TextField
                        label="Hasło"
                        type="password"
                        fullWidth
                        disabled
                    />

                    <Button type="submit" variant="contained" fullWidth>
                        {buttonText}
                    </Button>
                </Stack>
            </form>
        </Paper>
    );
}
