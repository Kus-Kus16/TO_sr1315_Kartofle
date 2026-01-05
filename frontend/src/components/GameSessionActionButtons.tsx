import type {GameSessionTypePreview} from "../types/GameSessionType"
import { useContext, useState } from "react"
import { AuthContext } from "../auth/AuthContext"
import {
    Button,
    Stack,
    Tooltip,
} from "@mui/material"
import api from "../api/axios"
import ConfirmDialog from "./ConfirmDialog.tsx";
import {RefreshContext} from "./RefreshContext.tsx";
import {useNavigate} from "react-router-dom";

interface Props {
    session: GameSessionTypePreview
    setError: (msg: string) => void
}

export default function GameSessionActionButtons({ session, setError }: Props) {
    const auth = useContext(AuthContext)
    const refresh = useContext(RefreshContext)
    const navigate = useNavigate()

    const [openDeleteDialog, setOpenDeleteDialog] = useState(false)

    const user = session.participants.find(
        p => p.username === auth.username
    );

    const isPastSession = new Date(session.date) < new Date()
    const isParticipant = user != undefined;
    const isFull = session.participants.length >= session.numberOfPlayers
    const isOwner = session.ownerId == user?.id

    const isJoinDisabled =
        !auth.isAuthenticated || isPastSession || isParticipant || isFull

    const isLeaveDisabled =
        !auth.isAuthenticated || isPastSession || !isParticipant || isOwner

    const getTooltip = () => {
        if (!auth.isAuthenticated) return "Musisz być zalogowany"
        if (isPastSession) return "Sesja już się odbyła"
        if (!isParticipant && isFull) return "Sesja jest pełna"
        return ""
    }

    const handleJoinLeave = async () => {
        try {
            if (isParticipant) {
                await api.delete(`/sessions/${session.id}/participants`)
            } else {
                await api.patch(`/sessions/${session.id}/participants`)
            }
            refresh.refresh()
        } catch {
            setError(
                isParticipant
                    ? "Nie udało się opuścić sesji."
                    : "Nie udało się dołączyć do sesji."
            )
        }
    }

    const handleDelete = async () => {
        try {
            await api.delete(`/sessions/${session.id}`)
            refresh.refresh()
            navigate("/sessions")
        } catch {
            setError("Nie udało się usunąć sesji.")
        }
    }

    return (
        <>
            <Stack direction="row" spacing={2}>
                {isOwner ? (
                    <Button
                        size="large"
                        variant="contained"
                        color="warning"
                        onClick={() => setOpenDeleteDialog(true)}
                        sx={{ width: "105px" }}
                    >
                        Usuń
                    </Button>
                ) : (
                    <Tooltip title={getTooltip()}>
                        <span>
                            <Button
                                size="large"
                                variant={isParticipant ? "outlined" : "contained"}
                                color={isParticipant ? "error" : "primary"}
                                onClick={handleJoinLeave}
                                disabled={isParticipant ? isLeaveDisabled : isJoinDisabled}
                                sx={{ width: "105px" }}
                            >
                                {isParticipant ? "Opuść" : "Dołącz"}
                            </Button>
                        </span>
                    </Tooltip>
                )}
            </Stack>

            <ConfirmDialog
                open={openDeleteDialog}
                title="Usuń sesję"
                description="Czy na pewno chcesz usunąć tę sesję? Tej operacji nie można cofnąć."
                confirmText={"Usuń"}
                onCancel={() => setOpenDeleteDialog(false)}
                onConfirm={() => {
                    setOpenDeleteDialog(false)
                    handleDelete().then()
                }}
            />
        </>
    )
}
