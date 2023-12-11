import axios from "./axios-config";
import {getJWT, setJWT} from "@/data";
import NotificationComponent from "@/components/general/NotificationComponent.vue";

export async function getAPI(endpoint) {
    const response = await axios.get(endpoint, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
    handleAPIRequest(response);
    return response;
}

export async function postAPI(endpoint, data = {}) {
    const response = await axios.post(endpoint, data, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
    handleAPIRequest(response);
    return response;
}

export async function putAPI(endpoint, data = {}) {
    const response = await axios.put(endpoint, data, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
    handleAPIRequest(response);
    return response;
}

export async function deleteAPI(endpoint) {
    const response = await axios.delete(endpoint, {
        headers: getHeaders(),
    }).catch(err => handleAPIError(err));
    handleAPIRequest(response);
    return response;
}

function handleAPIError(error) {
    const response = error?.response;
    // showUnsuccessfulNotification('Something went wrong, please try again');
    if (response?.status === 401 && location.pathname !== "/login") {
        location.assign("/login");
    }
    return response;
}

function handleAPIRequest(response, notification) {
    const newJWT = response?.headers?.authorization;
    // showSuccessfulNotification('Action successful');
    if (newJWT) {
        setJWT(newJWT);
    }
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

// export function showSuccessfulNotification(message) {
//     NotificationComponent.createSuccessfulNotification(message);
// }
//
// export function showUnsuccessfulNotification(message) {
//     NotificationComponent.createUnsuccessfulNotification(message);
// }