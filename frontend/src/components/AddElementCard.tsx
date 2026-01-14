import {Card, CardActionArea, CardContent, Typography} from "@mui/material";

interface AddElementCardProps {
    title: string;
    onClick: () => void;
}

export default function AddElementCard({ title, onClick }: AddElementCardProps) {
    return <Card
        sx={{
            minWidth: 240,
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
                <Typography variant="h3" align="center" color="primary">
                    ＋
                </Typography>
                <Typography align="center">
                    {title}
                </Typography>
            </CardContent>
        </CardActionArea>
    </Card>
}
