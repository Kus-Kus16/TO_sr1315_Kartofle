import { Box } from "@mui/material";
import type {ReactNode} from "react";

interface TightLayoutBoxProps {
    children: ReactNode;
}

export default function TightLayoutBox({ children }: TightLayoutBoxProps) {
    return (
        <Box sx={{ maxWidth: 700, margin: "auto", mt: 4 }}>
            {children}
        </Box>
    );
};

