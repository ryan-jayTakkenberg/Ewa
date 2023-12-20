import Team from "@/models/team";

export default class User {
    static users = [];

    static PermissionLevel = {VIEWER: "VIEWER", ADMIN: "ADMIN",}

    constructor(id, email, name, permissionLevel, lastLogin, password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.permissionLevel = permissionLevel;
        this.lastLogin = lastLogin;
        this.password = password;
    }

    clone() {
        return new User(this.id, this.email, this.name, this.permissionLevel, this.lastLogin, this.password);
    }

    injectAttributes(from) {
        if (!(from instanceof Object)) {
            return false;
        }
        for (let attr of Object.keys(this)) {
            if (from[attr] !== null && from[attr] !== undefined) {
                this[attr] = from[attr];
            }
        }
        return true;
    }

    equals(other) {
        if (!other) return false;
        for (let attr of Object.keys(this)) {
            if (typeof this[attr] !== typeof other[attr] || `${this[attr]}` !== `${other[attr]}`) return false;
        }
        return true;
    }

    getTeams() {
        return Team.teams.filter(team => team.users.includes(this));
    }

    static createNewUser(email, name, permissionLevel, lastLogin, password) {
        return new User(-1, email, name, permissionLevel, null, password);
    }
}
