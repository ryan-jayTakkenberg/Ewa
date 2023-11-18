import axios from "./axios-config";
import {getJWT} from "@/data";

export async function getAPI(endpoint) {
    return await axios.get(endpoint, {
        headers: {
            "Authorization": getJWT(),
        },
    }).catch(r => r?.response);
}

export async function postAPI(endpoint, data = {}) {
    return await axios.post(endpoint, data, {
        headers: {
            "Authorization": getJWT(),
        },
    }).catch(r => r?.response);
}

export async function putAPI(endpoint, data = {}) {
    return await axios.put(endpoint, data, {
        headers: {
            "Authorization": getJWT(),
        },
    }).catch(r => r?.response);
}

export async function deleteAPI(endpoint) {
    return await axios.delete(endpoint, {
        headers: {
            "Authorization": getJWT(),
        },
    }).catch(r => r?.response);
}

export function responseOk(response) {
    return response?.status && response.status >= 200 && response.status < 300;
}