import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import AppRouter from './app/AppRouter'
import AuthProvider from './context/AuthProvider'
import App from './App'
import { BrowserRouter } from 'react-router-dom'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <App>
          <AppRouter />
        </App>
      </AuthProvider>
    </BrowserRouter>
  </StrictMode>,
)
