import { Box } from "@mui/material";
import type {ReactNode} from "react";

interface WideLayoutBoxProps {
    children: ReactNode;
}

export default function WideLayoutBox({ children }: WideLayoutBoxProps) {
    return (
        <Box sx={{ maxWidth: 1200, margin: "auto", mt: 4 }}>
            {children}
        </Box>
    );
};

