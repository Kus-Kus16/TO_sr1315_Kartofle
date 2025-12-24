import { Outlet } from "react-router-dom";
import Box from "@mui/material/Box";
import Navbar from "./Navbar";

export default function MainLayout() {
    return (
        <Box sx={{ display: "flex", flexDirection: "column"}}>
            <Navbar />

            <Box sx={{ flex: 1, mt: "64px", mb: "64px", overflowY: "auto", p: 3 }}>
                <Outlet />
            </Box>
        </Box>
    );
}
