import { Routes, Route } from "react-router-dom";
import MainLayout from "../layout/MainLayout";
import RequireAuth from "../auth/RequireAuth";
import BoardGames from "../pages/BoardGames/BoardGameList";
import BoardGameDetails from "../pages/BoardGames/BoardGameDetails.tsx";
import GameSessions from "../pages/GameSessions/GameSessions";
import GameSessionInsight from "../pages/GameSessions/GameSessionInsight";
import UserSessions from "../pages/GameSessions/UserSessions";
import HomePage from "../pages/HomePage.tsx";
import AuthPage from "../pages/Auth/AuthPage.tsx";

export default function AppRouter() {
    return (
        <Routes>

            <Route element={<MainLayout />}>

                {/* PUBLIC */}
                <Route path="/" element={<HomePage />} />

                <Route path="/login" element={<AuthPage />} />

                <Route path="/boardgames" element={<BoardGames />} />
                <Route path="/boardgames/:id" element={<BoardGameDetails />} />

                <Route path="/sessions" element={<GameSessions />} />
                <Route path="/sessions/:id" element={<GameSessionInsight />} />

                {/* PROTECTED CRUD */}
                <Route element={<RequireAuth />}>
                    <Route path="/sessions/user" element={<UserSessions />} />
                    {/* <Route path="/sessions/add" element={<AddGameSession />} /> */}
                    {/* <Route path="/boardgames/add" element={<AddBoardGame />} /> */}
                </Route>

            </Route>

        </Routes>
    );
}
