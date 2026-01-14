import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import AuthProvider from './auth/AuthProvider'
import App from './App'
import { BrowserRouter } from 'react-router-dom'
import {createTheme, ThemeProvider} from "@mui/material";

const theme = createTheme({
    palette: {
        primary: {
            main: "#1976d2",
            contrastText: "#fff",
        },
    },
    components: {
        MuiCard: {
            defaultProps: {
                elevation: 4,
            },
        },
    },
});


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
        <ThemeProvider theme={theme}>
            <AuthProvider>
                <App/>
            </AuthProvider>
        </ThemeProvider>
    </BrowserRouter>
  </StrictMode>,
)
