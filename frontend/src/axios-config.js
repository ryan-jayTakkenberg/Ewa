import axios from "axios";

// Changing the port to 8081
const instance = axios.create({
    baseURL: "http://localhost:8085",
});

export default instance;