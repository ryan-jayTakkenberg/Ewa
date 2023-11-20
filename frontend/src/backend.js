import axios from "./axios-config";
import {getJWT} from "@/data";

export async function getAPI(endpoint) {
    return await axios.get(endpoint, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
}

export async function postAPI(endpoint, data = {}) {
    return await axios.post(endpoint, data, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
}

export async function putAPI(endpoint, data = {}) {
    return await axios.put(endpoint, data, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
}

export async function deleteAPI(endpoint) {
    return await axios.delete(endpoint, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
}

function handleAPIError(error) {
    const response = error?.response;
    if (response?.status === 401 && location.pathname !== "/login") {
        location.assign("/login");
    }
    return response;
}

export function getHeaders() {
    return {
        "Authorization": getJWT(),
        "Content-Type": "application/json",
    }
}

export function responseOk(response) {
    return response?.status && response.status >= 200 && response.status < 300;
}