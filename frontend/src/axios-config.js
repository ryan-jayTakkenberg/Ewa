import axios from "axios";

const instance = axios.create({
    baseURL: /*'https://ewa-back-end-jcat.onrender.com/'*/  "http://localhost:8085"
});

export default instance;