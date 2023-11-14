export function setKey(key) {
    localStorage.setItem("key", key);
}

export function getKey() {
    return localStorage.getItem("key") ?? "";
}

export function setAdmin(bool) {
    localStorage.setItem("admin", bool);
}

export function isAdmin() {
    return (localStorage.getItem("admin") ?? "false") === "true";
}