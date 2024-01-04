export default class User {

    static currentUser = null;
    static users = [];
    static PermissionLevel = {VIEWER: "VIEWER", ADMIN: "ADMIN",}

    constructor(id, email, name, permissionLevel, lastLogin, password, team) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.permissionLevel = permissionLevel;
        this.lastLogin = lastLogin;
        this.password = password;
        this.team = team;
    }

    clone() {
        return new User(...Object.values(this));
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

    static createNewUser(email, name, permissionLevel, lastLogin, password, team) {
        return new User(null, email, name, permissionLevel, null, password, team);
    }

    static getUserById(id) {
        return User.users.find(user => user.id === id);
    }

    static getLoggedInUserTeam(id) {
        const loggedInUser = User.getUserById(id);
        // console.log(loggedInUser);
        // console.log(User.users);

        if (loggedInUser) {
            const team = loggedInUser.team;
            return team ? team : "Currently not in a team";
        } else {
            return "User not found";
        }
    }
}
