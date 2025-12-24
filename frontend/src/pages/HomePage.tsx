import { Box, Typography, Button, Stack } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";

export default function HomePage() {
    return (
        <Box
            sx={{
                maxWidth: 800,
                mx: "auto",
                mt: 8,
                textAlign: "center",
            }}
        >
            <Typography variant="h3" gutterBottom>
                Organizuj sesje gier planszowych
            </Typography>

            <Typography variant="h6" color="text.secondary">
                Przeglądaj katalog gier planszowych, twórz sesje i dołączaj do
                rozgrywek z innymi graczami.
            </Typography>

            <Stack
                direction={{ xs: "column", sm: "row" }}
                spacing={2}
                justifyContent="center"
                mt={4}
            >
                <Button
                    variant="contained"
                    size="large"
                    component={RouterLink}
                    to="/boardgames"
                >
                    Zobacz gry
                </Button>

                <Button
                    variant="outlined"
                    size="large"
                    component={RouterLink}
                    to="/sessions"
                >
                    Zobacz sesje
                </Button>
            </Stack>
        </Box>
    );
}
