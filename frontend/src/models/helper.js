export function classToObject(c) {
    let obj = {};
    if (!c) {
        return obj;
    }
    for (let key of Object.keys(c)) {
        if (key) {
            obj[key] = c[key];
        }
    }
    return obj;
}