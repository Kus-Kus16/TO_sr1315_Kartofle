import { Routes, Route } from "react-router-dom";
import MainLayout from "../layout/MainLayout";
import RequireAuth from "../auth/RequireAuth";
import BoardGames from "../pages/BoardGames/BoardGameList";
import BoardGameDetails from "../pages/BoardGames/BoardGameDetails.tsx";
import GameSessions from "../pages/GameSessions/GameSessionList.tsx";
import GameSessionInsight from "../pages/GameSessions/GameSessionDetails.tsx";
import HomePage from "../pages/HomePage.tsx";
import AuthPage from "../pages/Auth/AuthPage.tsx";
import BoardGameAdd from "../pages/BoardGames/BoardGameAdd.tsx";
import BoardGameEdit from "../pages/BoardGames/BoardGameEdit.tsx";
import GameSessionListUser from "../pages/GameSessions/GameSessionListUser.tsx";
import GameSessionAdd from "../pages/GameSessions/GameSessionAdd.tsx";

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
                    <Route path="/boardgames/new" element={<BoardGameAdd />} />
                    <Route path="/boardgames/:id/edit" element={<BoardGameEdit />} />
                    <Route path="/sessions/user" element={<GameSessionListUser />} />
                    <Route path="/sessions/new" element={<GameSessionAdd/>} />
                </Route>

            </Route>

        </Routes>
    );
}
