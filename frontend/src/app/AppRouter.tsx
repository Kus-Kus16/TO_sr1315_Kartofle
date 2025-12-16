import { Route, Routes } from 'react-router-dom'
import Login from '../pages/Login/Login'
import Register from '../pages/Login/Register'
import BoardGames from '../pages/BoardGames/BoardGameList'
import GameSesions from '../pages/GameSessions/GameSessions'
import BoardGameInsight from '../pages/BoardGames/BoardGameInsight'
import AddGameSession from '../pages/GameSessions/AddGameSession'
import UserSessions from '../pages/GameSessions/UserSessions'

export default function AppRouter() {
  return (
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/boardgames" element={<BoardGames />} />
        <Route path="/game-sessions" element={<GameSesions />} />
        <Route path="/boardgames/:id" element={<BoardGameInsight />} />
        <Route path="/game-sessions/add" element={<AddGameSession />} />
        <Route path="/user-game-sessions" element={<UserSessions />} />
      </Routes>
  )
}