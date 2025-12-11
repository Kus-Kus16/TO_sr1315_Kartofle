import { BrowserRouter, Route, Routes } from 'react-router-dom'
import App from '../App'
import Login from '../pages/Login/Login'
import Register from '../pages/Login/Register'
import BoardGames from '../pages/BoardGames/BoardGames'
import GameSesions from '../pages/GameSessions/GameSessions'

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App/>} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/board-games" element={<BoardGames />} />
        <Route path="/game-sessions" element={<GameSesions />} />
      </Routes>
    </BrowserRouter>
  )
}