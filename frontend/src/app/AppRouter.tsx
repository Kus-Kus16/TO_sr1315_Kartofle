import { Route, Routes } from 'react-router-dom'
import Login from '../pages/Login/Login'
import Register from '../pages/Login/Register'
import BoardGames from '../pages/BoardGames/BoardGameList'
import GameSesions from '../pages/GameSessions/GameSessions'
import BoardGameInsight from '../pages/BoardGames/BoardGameInsight'
import UserSessions from '../pages/GameSessions/UserSessions'
import GameSessionInsight from '../pages/GameSessions/GameSessionInsight'

export default function AppRouter() {
  return (
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/boardgames" element={<BoardGames />} />
        <Route path="/sessions" element={<GameSesions />} />
        <Route path="/boardgames/:id" element={<BoardGameInsight />} />
        {/* <Route path="/sessions/add" element={<AddGameSession />} /> */}
        <Route path="/sessions/user" element={<UserSessions />} />
        <Route path="/sessions/:id" element={<GameSessionInsight />} />
      </Routes>
  )
}