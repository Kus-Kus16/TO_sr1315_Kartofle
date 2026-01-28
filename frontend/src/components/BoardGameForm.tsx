import {
    CardActions,
    TextField,
    Button,
    Stack,
    Chip
} from "@mui/material";
import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { PictureAsPdf, Photo } from '@mui/icons-material';
import type { BoardGameTypeDetails } from "../types/BoardGameType.ts";
import {baseURL} from "../api/axios.tsx";

export interface BoardGameFormData {
    title: string;
    description: string;
    minPlayers: number;
    maxPlayers: number;
    minutesPlaytime: number;

    imageFile: File | null;
    rulebookFile: File | null;

    existingImageUrl?: string;
    existingRulebookUrl?: string;
}

export interface BoardGameFormProps {
    initialData?: BoardGameTypeDetails;
    onSubmit: (formData: BoardGameFormData) => void;
    setError: (msg: string) => void;
    setImagePreview: (preview: string) => void;
    disableEdit: boolean;
}

const MAX_PDF_SIZE_MB = 10;
const MAX_IMAGE_SIZE_MB = 5;
const ALLOWED_IMAGE_TYPES = ['image/jpeg', 'image/png', 'image/gif'];

export default function BoardGameForm({ initialData, onSubmit, setError, setImagePreview, disableEdit } : BoardGameFormProps) {
    const navigate = useNavigate();
    const imgInputRef = useRef<HTMLInputElement | null>(null);
    const pdfInputRef = useRef<HTMLInputElement | null>(null);

    const [form, setForm] = useState<BoardGameFormData>({
        title: initialData?.title || "",
        description: initialData?.description || "",
        minPlayers: initialData?.minPlayers || 1,
        maxPlayers: initialData?.maxPlayers || 1,
        minutesPlaytime: initialData?.minutesPlaytime || 30,
        imageFile: null,
        rulebookFile: null,
        existingImageUrl: initialData?.imageUrl,
        existingRulebookUrl: initialData?.rulebookUrl
    });

    const [fieldErrors, setFieldErrors] = useState<{
        title?: string;
        minPlayers?: string;
        maxPlayers?: string;
        minutesPlaytime?: string;
    }>({});

    const [hasImage, setHasImage] = useState<boolean>(!!initialData?.imageUrl);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;

        setForm(prev => ({
            ...prev,
            [name]: name.includes("Players") || name === "minutesPlaytime"
                ? Number(value)
                : value
        }));
    };

    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (!ALLOWED_IMAGE_TYPES.includes(file.type)) {
            setError("Niedozwolone rozszerzenie pliku: " + file.type);
            e.target.value = '';
            return;
        }

        if (file.size > MAX_IMAGE_SIZE_MB * 1024 * 1024) {
            setError(`Plik nie może być większy niż ${MAX_IMAGE_SIZE_MB} MB`);
            e.target.value = '';
            return;
        }

        setForm(prev => ({ ...prev, imageFile: file, existingImageUrl: undefined }));
        setImagePreview(URL.createObjectURL(file));
        setHasImage(true)
    };

    const handleRulebookChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (file.type !== 'application/pdf') {
            setError("Niedozwolone rozszerzenie pliku: " + file.type);
            e.target.value = '';
            return;
        }

        if (file.size > MAX_PDF_SIZE_MB * 1024 * 1024) {
            setError(`Plik nie może być większy niż ${MAX_PDF_SIZE_MB} MB`);
            e.target.value = '';
            return;
        }

        setForm(prev => ({ ...prev, rulebookFile: file, existingRulebookUrl: undefined }));
    };

    const removeExistingImage = () => {
        setForm(prev => ({ ...prev, existingImageUrl: undefined, imageFile: null }));
        setImagePreview("");
        setHasImage(false);
        if (imgInputRef.current) imgInputRef.current.value = "";
    };

    const removeExistingRulebook = () => {
        setForm(prev => ({ ...prev, existingRulebookUrl: undefined, rulebookFile: null }));
        if (pdfInputRef.current) pdfInputRef.current.value = "";
    };

    const validateFields = () => {
        const errors: typeof fieldErrors = {};

        if (!form.title.trim()) {
            errors.title = "Tytuł jest wymagany";
        }
        if (form.minPlayers < 1) {
            errors.minPlayers = "Minimalna liczba graczy to 1";
        }
        if (form.maxPlayers < form.minPlayers) {
            errors.maxPlayers = "Maksimum graczy nie może być mniejsze niż minimum";
        }
        if (form.minutesPlaytime < 1) {
            errors.minutesPlaytime = "Czas gry musi być większy niż 0";
        }

        setFieldErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (validateFields()) {
            onSubmit(form);
        }
    };

    return (
        <Stack spacing={2} component="form" onSubmit={handleSubmit}>
            <TextField
                label="Tytuł"
                name="title"
                value={form.title}
                onChange={handleChange}
                required
                fullWidth
                disabled={disableEdit}
                error={!!fieldErrors.title}
                helperText={fieldErrors.title}
            />

            <TextField
                label="Opis"
                name="description"
                value={form.description}
                onChange={handleChange}
                multiline
                rows={4}
                fullWidth
            />

            <Stack direction="row" spacing={2}>
                <TextField
                    label="Min. graczy"
                    name="minPlayers"
                    type="number"
                    value={form.minPlayers}
                    onChange={handleChange}
                    slotProps={{
                        input: {
                            inputProps: {
                                min: 1,
                                max: form.maxPlayers
                            }
                        }
                    }}
                    required
                    fullWidth
                    disabled={disableEdit}
                    error={!!fieldErrors.minPlayers}
                    helperText={fieldErrors.minPlayers}
                />

                <TextField
                    label="Max. graczy"
                    name="maxPlayers"
                    type="number"
                    value={form.maxPlayers}
                    onChange={handleChange}
                    slotProps={{
                        input: {
                            inputProps: {
                                min: form.minPlayers,
                                max: 20
                            }
                        }
                    }}
                    required
                    fullWidth
                    disabled={disableEdit}
                    error={!!fieldErrors.maxPlayers}
                    helperText={fieldErrors.maxPlayers}
                />
            </Stack>

            <TextField
                label="Czas gry (minuty)"
                name="minutesPlaytime"
                type="number"
                value={form.minutesPlaytime}
                onChange={handleChange}
                slotProps={{
                    input: { inputProps: { min: 1, max: 600 } }
                }}
                required
                fullWidth
                error={!!fieldErrors.minutesPlaytime}
                helperText={fieldErrors.minutesPlaytime}
            />

            <Button variant="outlined" component="label">
                {hasImage ? "Zmień obrazek" : "Dodaj obrazek"}
                <input
                    type="file"
                    hidden
                    accept="image/*"
                    ref={imgInputRef}
                    onChange={handleImageChange}
                />
            </Button>

            {hasImage && (
                <Chip
                    icon={<Photo />}
                    label={form.imageFile?.name || "Aktualny obrazek"}
                    onDelete={removeExistingImage}
                    color="secondary"
                    variant="outlined"
                />
            )}

            <Button variant="outlined" component="label">
                {form.rulebookFile || form.existingRulebookUrl
                    ? "Zmień instrukcję"
                    : "Dodaj instrukcję (PDF)"}
                <input
                    type="file"
                    hidden
                    accept="application/pdf"
                    ref={pdfInputRef}
                    onChange={handleRulebookChange}
                />
            </Button>

            {(form.rulebookFile || form.existingRulebookUrl) && (
                <Chip
                    icon={<PictureAsPdf />}
                    label={form.rulebookFile?.name || "Aktualna instrukcja"}
                    component={"a"}
                    href={`${baseURL}${form.existingRulebookUrl}`}
                    onDelete={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                        removeExistingRulebook();
                    }}
                    color="secondary"
                    variant="outlined"
                />
            )}

            <CardActions sx={{ mt: 2, px: 0 }}>
                <Stack direction="row" spacing={2}>
                    <Button type="submit" variant="contained">
                        Zapisz
                    </Button>
                    <Button color="error" variant="outlined" onClick={() => navigate(-1)}>
                        Anuluj
                    </Button>
                </Stack>
            </CardActions>
        </Stack>
    );
}
