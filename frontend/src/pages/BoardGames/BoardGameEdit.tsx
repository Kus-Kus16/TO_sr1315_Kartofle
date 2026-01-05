import api from "../../api/axios.tsx";
import {useNavigate, useParams} from "react-router-dom";
import BoardGameForm, {type BoardGameFormData} from "../../components/BoardGameForm.tsx";
import {useEffect, useState} from "react";
import {Alert, Box, CircularProgress} from "@mui/material";
import type {BoardGameTypeFull, BoardGameTypeUpdate} from "../../types/BoardGameType.ts";

export default function BoardGameEdit() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [initialData, setInitialData] = useState<BoardGameTypeFull | undefined>(undefined);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await api.get<BoardGameTypeFull>(`/boardgames/${id}`);
                setInitialData(response.data);
            } catch {
                setError("Nie udało się pobrać danych gry.");
            } finally {
                setLoading(false);
            }
        };

        fetchData().then();
    }, [id]);

    const handleEdit = async (
        formData: BoardGameFormData,
        setError: (msg: string) => void
    ) => {
        try {
            console.log(formData);
            const data = new FormData();

            data.append("description", formData.description);
            data.append("minutesPlaytime", formData.minutesPlaytime.toString());

            if (formData.imageFile) {
                data.append("imageFile", formData.imageFile);
            }
            if (formData.rulebookFile) {
                data.append("rulebookFile", formData.rulebookFile);
            }

            await api.patch<BoardGameTypeUpdate>(`/boardgames/${id}`, data, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            navigate("/boardgames");
        } catch {
            setError("Nie udało się zaktualizować plików gry.");
        }
    };

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return <Alert severity="error">{error}</Alert>;
    }

    if (!initialData) {
        setError("Nie znaleziono gry o podanym ID");
        return null
    }

    return <BoardGameForm initialData={initialData} onSubmit={handleEdit} formTitle={"Edytuj grę"} disableEdit={true}/>
}