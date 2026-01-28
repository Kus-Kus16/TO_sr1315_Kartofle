import apiMultipart from "../../api/axiosMultipart.tsx";
import {useNavigate} from "react-router-dom";
import {type BoardGameFormData} from "../../components/BoardGameForm.tsx";
import type {BoardGameTypeCreate} from "../../types/BoardGameType.ts";
import {useState} from "react";
import TightLayoutBox from "../../layout/TightLayoutBox.tsx";
import {Alert, Box, Card, CardContent, Stack, Tab, Typography} from "@mui/material";
import BoardGameForm from "../../components/BoardGameForm.tsx";
import ImageMedia from "../../components/ImageMedia.tsx";
import {TabContext, TabList, TabPanel} from '@mui/lab';
import BoardGameImportForm from "../../components/BoardGameImportForm.tsx";

export default function BoardGameAdd() {
    const navigate = useNavigate();
    const [imagePreview, setImagePreview] = useState("");
    const [tabsValue, setTabsValue] = useState(1);

    const [error, setError] = useState("");

    const handleAdd = async (formData: BoardGameFormData) => {
        try {
            const data = new FormData();

            data.append("title", formData.title);
            data.append("description", formData.description);
            data.append("minPlayers", formData.minPlayers.toString());
            data.append("maxPlayers", formData.maxPlayers.toString());
            data.append("minutesPlaytime", formData.minutesPlaytime.toString());

            if (formData.imageFile) {
                data.append("imageFile", formData.imageFile);
            }
            if (formData.rulebookFile) {
                data.append("rulebookFile", formData.rulebookFile);
            }

            await apiMultipart.post<BoardGameTypeCreate>("/boardgames", data);

            navigate("/boardgames");
        } catch {
            setError("Nie udało się dodać gry.");
        }
    };

    return (
        <TightLayoutBox>
            <Card>
                <ImageMedia displayImage={!!imagePreview} imageUrl={imagePreview} height={300} width={700}/>

                <CardContent>
                    <Stack spacing={2}>
                        <Typography variant="h4" gutterBottom>
                            Dodaj nową grę
                        </Typography>

                        {error && <Alert severity="error">{error}</Alert>}

                        <TabContext value={tabsValue}>
                            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                                <TabList onChange={(_, value) => setTabsValue(value)}>
                                    <Tab label="Wpisz dane" value={1}/>
                                    <Tab label="Importuj" value={2}/>
                                </TabList>
                            </Box>

                            <TabPanel value={1}>
                                <BoardGameForm onSubmit={handleAdd} setError={setError}
                                               setImagePreview={setImagePreview} disableEdit={false}
                                />
                            </TabPanel>
                            <TabPanel value={2}>
                                <BoardGameImportForm setError={setError}/>
                            </TabPanel>

                        </TabContext>
                    </Stack>
                </CardContent>
            </Card>
        </TightLayoutBox>
    );
}