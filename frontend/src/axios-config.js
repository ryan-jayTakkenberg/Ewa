import axios from "axios";

// Base url and port for backend
const instance = axios.create({
    baseURL: "http://localhost:8085",
});

export default instance;