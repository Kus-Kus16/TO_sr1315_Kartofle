import { Link } from 'react-router-dom'

export default function App({ children }: { children: React.ReactNode }) {

  return (
    <div>
      <div>Main page</div>
      <Link to="/login">Login</Link><br/>
      <Link to="/register">Register</Link><br/>
      <Link to="/boardgames">Board Games</Link><br/>
      <Link to="/game-sessions">Game Sessions</Link><br/>
      <Link to="/game-sessions/add">Add Game Session</Link><br/>
      <Link to="/user-game-sessions">My Game Sessions</Link><br/>
      <hr/>
      {children}
    </div>
  )
}