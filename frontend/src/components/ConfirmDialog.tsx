import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogContentText,
    DialogActions,
    Button
} from "@mui/material"

interface ConfirmDialogProps {
    open: boolean
    title: string
    description: string
    onCancel: () => void
    onConfirm: () => void
    confirmText: string
}

export default function ConfirmDialog({ open, title, description, onCancel, onConfirm, confirmText }: ConfirmDialogProps) {
    return (
        <Dialog open={open} onClose={onCancel}>
            <DialogTitle>{title}</DialogTitle>

            <DialogContent>
                <DialogContentText>
                    {description}
                </DialogContentText>
            </DialogContent>

            <DialogActions>
                <Button onClick={onCancel}>Anuluj</Button>
                <Button color="error" variant="contained" onClick={onConfirm}>
                    {confirmText}
                </Button>
            </DialogActions>
        </Dialog>
    )
}
