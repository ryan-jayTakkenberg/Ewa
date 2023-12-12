export function setJWT(jwt) {
    localStorage.setItem("jwt", jwt);
}

export function getJWT() {
    return localStorage.getItem("jwt") ?? "";
}

export function removeJWT() {
    localStorage.removeItem("jwt");
}

export function setAdmin(bool) {
    localStorage.setItem("admin", bool);
}

export function isAdmin() {
    return (localStorage.getItem("admin") ?? "false") === "true";
}

export function removeAdmin() {
    localStorage.removeItem("admin");
}

export function setId(id) {
    return localStorage.setItem("id", id);
}

export function getId() {
    return Number(localStorage.getItem("id")) ?? NaN;
}

export function removeId() {
    return localStorage.removeItem("id");
}

export function setUsername(id) {
    return localStorage.setItem("username", id);
}

export function getUsername() {
    return localStorage.getItem("username") ?? "";
}

export function removeAll() {
    localStorage.clear();
}