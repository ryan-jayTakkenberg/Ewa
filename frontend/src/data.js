export function setKey(key) {
    localStorage.setItem("key", key);
}

export function getKey() {
    return localStorage.getItem("key") ?? "550e8400-e29b-41d4-a716-446655440000";// 550e8400-e29b-41d4-a716-446655440000 = debug admin key
}