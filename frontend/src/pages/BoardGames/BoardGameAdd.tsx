import apiMultipart from "../../api/axiosMultipart.tsx";
import {useNavigate} from "react-router-dom";
import BoardGameForm, {type BoardGameFormData} from "../../components/BoardGameForm.tsx";
import type {BoardGameTypeCreate} from "../../types/BoardGameType.ts";

export default function BoardGameAdd() {
    const navigate = useNavigate();

    const handleAdd = async (
        formData: BoardGameFormData,
        setError: (msg: string) => void
    ) => {
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

            for (const [key, value] of data.entries()) {
                console.log(key, value);
            }
            await apiMultipart.post<BoardGameTypeCreate>("/boardgames", data);

            navigate("/boardgames");
        } catch {
            setError("Nie udało się dodać gry.");
        }
    };

    return <BoardGameForm onSubmit={handleAdd} formTitle={"Dodaj nową grę"} disableEdit={false}/>
}