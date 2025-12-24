import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { Box, Grid } from "@mui/material";
import {AuthContext} from "../../auth/AuthContext.tsx";
import AuthForm from "../../components/AuthForm.tsx";
import api from "../../api/axios"
import axios from "axios";

export default function AuthPage() {
    const auth = useContext(AuthContext);
    const navigate = useNavigate();


    const handleAuthForm = async (url: string, username: string, setError: (msg: string) => void) => {
        try {
            const { data } = await api.post(url, { username });
            auth.login(data.username);
            navigate("/");
        } catch (err: unknown) {
            if (axios.isAxiosError(err)) {
                setError(err.response?.data?.message || "Wystąpił błąd");
            } else if (err instanceof Error) {
                setError(err.message);
            } else {
                setError("Wystąpił nieznany błąd");
            }
        }
    };

    const handleRegister = async (username: string, setError: (msg: string) => void) => {
        await handleAuthForm("/users", username, setError);
    }

    const handleLogin = async (username: string, setError: (msg: string) => void) => {
        await handleAuthForm("/users/login", username, setError);
    }

    return (
        <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "75vh", backgroundColor: "#f5f5f5" }}>
            <Grid container spacing={4} justifyContent="center" alignItems="center">
                <Grid>
                    <AuthForm
                        title="Logowanie"
                        buttonText="Zaloguj się"
                        onSubmit={handleLogin}
                    />
                </Grid>
                <Grid>
                    <AuthForm
                        title="Rejestracja"
                        buttonText="Zarejestruj się"
                        onSubmit={handleRegister}
                    />
                </Grid>
            </Grid>
        </Box>
    )
}
