import * as React from 'react';
import { useContext } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Avatar from '@mui/material/Avatar';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import IconButton from '@mui/material/IconButton';
import { Link as RouterLink } from 'react-router-dom';
import { AuthContext } from '../auth/AuthContext';
import { ListItemText } from "@mui/material";

const pages = [
    { label: 'Gry', path: '/boardgames' },
    { label: 'Sesje', path: '/sessions' }
];

export default function Navbar() {
    const auth = useContext(AuthContext);
    const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);

    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };
    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    function stringToColor(string: string) {
        let hash = 0;
        let i;

        for (i = 0; i < string.length; i += 1) {
            hash = string.charCodeAt(i) + ((hash << 5) - hash);
        }

        let color = '#';

        for (i = 0; i < 3; i += 1) {
            const value = (hash >> (i * 8)) & 0xff;
            color += `00${value.toString(16)}`.slice(-2);
        }

        return color;
    }

    return (
        <AppBar position="fixed">
            <Toolbar>
                <Typography
                    variant="h6"
                    component={RouterLink}
                    to="/"
                    sx={{ textDecoration: 'none', color: 'inherit', mr: 4 }}
                >
                    🎲 Board Game Gather
                </Typography>

                <Box sx={{ flexGrow: 1 }}>
                    {pages.map((page) => (
                        <Button
                            key={page.path}
                            component={RouterLink}
                            to={page.path}
                            color="inherit"
                        >
                            {page.label}
                        </Button>
                    ))}
                </Box>

                {/* Login / Avatar */}
                {auth?.username ? (
                    <Box sx={{ flexGrow: 0 }}>
                        <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                            <Avatar sx={{ bgcolor: stringToColor(auth.username) }}>
                                {auth.username[0].toUpperCase()}
                            </Avatar>
                        </IconButton>
                        <Menu
                            sx={{ mt: '45px' }}
                            anchorEl={anchorElUser}
                            anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                            transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >
                            <MenuItem disabled>
                                <ListItemText
                                    primary="Zalogowany jako"
                                    secondary={auth.username}
                                    slotProps={{
                                        secondary: {
                                            fontWeight: 600,
                                        },
                                    }}
                                />
                            </MenuItem>
                            <MenuItem
                                component={RouterLink}
                                to="/sessions/user"
                                onClick={handleCloseUserMenu}
                            >
                                Moje sesje
                            </MenuItem>
                            <MenuItem
                                onClick={() => { auth.logout(); handleCloseUserMenu(); }}
                            >
                                Wyloguj
                            </MenuItem>
                        </Menu>
                    </Box>
                ) : (
                    <Button component={RouterLink} to="/login" color="inherit">
                        Zaloguj się
                    </Button>
                )}
            </Toolbar>
        </AppBar>
    );
}
