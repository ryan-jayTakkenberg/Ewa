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
    if (response?.status === 401 && location.pathname !== "/login") {
        location.assign("/login");
    } else {
        showUnsuccessfulNotification('Something went wrong, please try again');
    }
    return response;
}

function handleAPIRequest(response) {
    if (response?.status && response.status >= 200 && response.status < 300) {
        if (response.config?.method === 'post') {
            if (response.config.url.includes('/login')) {
                // Handle login success differently
                showSuccessfulNotification('Login successful');
            } else {
                // Handle other successful requests
                showSuccessfulNotification('Create successful');
            }
        } else if (response.config?.method === 'put') {
            showSuccessfulNotification('Update successful');
        } else if (response.config?.method === 'delete') {
            showSuccessfulNotification('Delete successful');
        }
    }

    const newJWT = response?.headers?.authorization;
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
    return !!(response?.status && response.status >= 200 && response.status < 300);
}

function showSuccessfulNotification(message) {
    NotificationComponent.methods.createSuccessfulNotification(message);
}

function showUnsuccessfulNotification(message) {
    NotificationComponent.methods.createUnsuccessfulNotification(message);
}