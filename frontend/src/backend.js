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
        showUnsuccessfulNotification('Something went wrong, please try again !!!');
    }
    return response;
}

function handleAPIRequest(response, notification) {
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
    if (response?.status && response.status >= 200 && response.status < 300) {
        if (response.config?.method !== 'get') {
            showSuccessfulNotification(':) Success ----------------------------------------------------------------');
        }
        return true;
    }
    return false;
}

function showSuccessfulNotification(message) {
    // Only allow one notification per second (1000ms)
    if (applyNotificationLimiter(1000)) {
        NotificationComponent.staticMethods.createSuccessfulNotification(message);
    }
}

function showUnsuccessfulNotification(message) {
    // Only allow one notification per second (1000ms)
    if (applyNotificationLimiter(1000)) {
        NotificationComponent.staticMethods.createUnsuccessfulNotification(message);
    }
}

let lastNotificationTime = 0;
function applyNotificationLimiter(ms) {
    if (lastNotificationTime > Date.now() - ms) {
        return false;
    }
    lastNotificationTime = Date.now();
    return true;
}