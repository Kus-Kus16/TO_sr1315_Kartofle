import axios from "axios";
import { baseURL } from "./axios";

const apiMultipart = axios.create({
    baseURL,
});

apiMultipart.interceptors.request.use((config) => {
    const login = localStorage.getItem("username");
    if (login) {
        config.headers["X-User-Login"] = login;
    }
    return config;
});

export default apiMultipart;
