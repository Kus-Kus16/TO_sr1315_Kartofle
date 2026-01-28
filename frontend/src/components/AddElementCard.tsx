import {Card, CardActionArea, CardContent, Typography} from "@mui/material";

interface AddElementCardProps {
    title: string;
    onClick: () => void;
    minWidth?: number;
    maxWidth?: number;
}

export default function AddElementCard({ title, onClick, minWidth, maxWidth }: AddElementCardProps) {
    return <Card
        sx={{
            minWidth: minWidth,
            maxWidth: maxWidth,
            height: "100%",
        }}
        onClick={onClick}
    >
        <CardActionArea
            sx={{
                height: "100%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
            }}
        >
            <CardContent>
                <Typography variant="h2" align="center" color="primary" sx={{ fontWeight: 400 }}>
                    ＋
                </Typography>
                <Typography align="center">
                    {title}
                </Typography>
            </CardContent>
        </CardActionArea>
    </Card>
}
