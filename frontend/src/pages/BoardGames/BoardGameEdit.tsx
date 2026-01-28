import api, {baseURL} from "../../api/axios.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {type BoardGameFormData} from "../../components/BoardGameForm.tsx";
import {useEffect, useState} from "react";
import {Alert, Box, Card, CardContent, CircularProgress, Stack, Typography} from "@mui/material";
import type {BoardGameTypeDetails, BoardGameTypeUpdate} from "../../types/BoardGameType.ts";
import TightLayoutBox from "../../layout/TightLayoutBox.tsx";
import BoardGameForm from "../../components/BoardGameForm.tsx";
import ImageMedia from "../../components/ImageMedia.tsx";

export default function BoardGameEdit() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [initialData, setInitialData] = useState<BoardGameTypeDetails | undefined>(undefined);
    const [imagePreview, setImagePreview] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await api.get<BoardGameTypeDetails>(`/boardgames/${id}`);
                const data = response.data;
                setInitialData(data);

                setImagePreview(data.imageUrl
                    ? `${baseURL}${data.imageUrl}`
                    : ""
                );
                if (data.discontinued) {
                    setError("Gra nie jest już dostępna");
                }
            } catch {
                setError("Nie udało się pobrać danych gry.");
            } finally {
                setLoading(false);
            }
        };

        fetchData().then();
    }, [id]);

    const handleEdit = async (formData: BoardGameFormData) => {
        try {
            const data = new FormData();

            data.append("description", formData.description);
            data.append("minutesPlaytime", formData.minutesPlaytime.toString());

            // Remove previous
            data.append("removeImage", formData.existingImageUrl ? "0" : "1")
            data.append("removeRulebook", formData.existingRulebookUrl ? "0" : "1")

            // Add new
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

    return (
        <TightLayoutBox>
            <Card>
                <ImageMedia displayImage={!!imagePreview} imageUrl={imagePreview} height={300} width={700}/>

                <CardContent>
                    <Stack spacing={2}>
                        <Typography variant="h4" gutterBottom>
                            Edytuj grę
                        </Typography>

                        {error && <Alert severity="error">{error}</Alert>}

                        <BoardGameForm onSubmit={handleEdit} setError={setError}
                                       setImagePreview={setImagePreview} disableEdit={true}
                                       initialData={initialData}
                        />
                    </Stack>
                </CardContent>
            </Card>
        </TightLayoutBox>
    );
}