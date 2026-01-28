import axios from 'axios';

export const baseURL = 'http://localhost:8080';

const api = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json',
    },
});

api.interceptors.request.use((config) => {
    const login = sessionStorage.getItem('username')
    if (login) {
        config.headers['X-User-Login'] = login
    }
    return config
})

export default api;
