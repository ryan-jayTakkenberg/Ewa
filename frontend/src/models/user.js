import Team from "@/models/team";
import axios from "@/axios-config";
import {getJWT} from "@/data";
import {classToObject} from "@/models/helper";
import {deleteAPI, postAPI, putAPI, responseOk} from "@/backend";

export default class User {

    static PermissionLevel = {
        VIEWER: "VIEWER",
        ADMIN: "ADMIN",
    }

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
        if (!other) {
            return false;
        }
        for (let attr of Object.keys(this)) {
            if (typeof this[attr] !== typeof other[attr] || `${this[attr]}` !== `${other[attr]}`) {
                return false;
            }
        }
        return true;
    }

    getTeams() {
        return Team.teams.filter(team => team.users.includes(this));
    }

    static createNewUser(email, name, permissionLevel, password) {
        return new User(-1, email, name, permissionLevel, password);
    }

    async postDatabase() {
        try {
            const isNewUser = this.id < 0;
            if (isNewUser) {
                return false;
            }

            let response = await postAPI('/api/users', classToObject(this));
            if (!responseOk(response)) {
                return;
            }

            this.injectAttributes(response.data);
            return this;
        } catch (e) {
            return null;
        }
    }

    /**
     * put this user into the database
     * will add a new user to the database if no user exists
     * will override the existing user with the new user if the user already exists
     */
    async putDatabase() {
        try {
            const isNewUser = this.id < 0;
            if (isNewUser) {
                return false;
            }

            let response = await putAPI(`/api/users/${this.id}`, classToObject(this));
            if (!responseOk(response)) {
                return;
            }

            // make a post request to the backend
            // if the current user id is -1, receive the new user id
            if (isNewUser) {
                this.id = response.data.id;// receive the new user id
                User.users.push(this);
            } else {
                User.users[User.users.findIndex(o => o.id === this.id)] = this;
            }
            this.injectAttributes(response.data);
            return this;
        } catch (e) {
            return null;
        }
    }


    /**
     * delete this user from the database
     */
    async delDatabase() {
        try {
            const isNewUser = this.id < 0;
            if (isNewUser) {
                return false;
            }

            // make a delete request to the backend
            if (!await deleteAPI(`/api/users/${this.id}`)) {
                return false;
            }

            User.users = User.users.filter(o => o.id !== this.id);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * get all the products from the database
     */
    static async getDatabase() {
        try {
            this.fetching = true;
            // make a get request to the backend
            // update "products" with the response
            let response = await axios.get("/api/users", {
                headers: {"Authorization": getJWT()}
            });

            let users = [];
            for (let obj of response.data) {
                let user = new User();
                if (user.injectAttributes(obj)) {
                    users.push(user);
                }
            }
            return users;
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static users = [];
}
