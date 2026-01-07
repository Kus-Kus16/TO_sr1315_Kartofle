import { useContext } from 'react'
import { Link } from 'react-router-dom'
import { AuthContext } from './context/AuthContext';

export default function App({ children }: { children: React.ReactNode }) {

  const auth = useContext(AuthContext);

  return (
    <div>
      <div>User: {auth?.username || "No user"}</div>
      <div>Main page</div>
      <Link to="/login">Login</Link><br/>
      <Link to="/register">Register</Link><br/>
      <Link to="/boardgames">Board Games</Link><br/>
      <Link to="/sessions">Game Sessions</Link><br/>
      {/* <Link to="/sessions/add">Add Game Session</Link><br/> */}
      <Link to="/sessions/user">My Game Sessions</Link><br/>
      <hr/>
      {children}
    </div>
  )
}