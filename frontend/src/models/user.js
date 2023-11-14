import Team from "@/models/team";
import axios from "@/axios-config";
import {getKey} from "@/data";
import {classToObject} from "@/models/helper";

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

    static createNewUser(email, name, permissionLevel, dateLastLoggedIn, password) {
        return new User(-1, email, name, permissionLevel, dateLastLoggedIn, password);
    }

    /**
     * put this product into the database
     * will add a new product to the database if no product exists
     * will override the existing product with the new product if the product already exists
     */
    async putDatabase() {
        try {
            const isNewUser= this.id < 0;


            let response = await axios.post("/api/users", classToObject(this), {
                headers: {
                    "Authorization": getKey(),
                    'Content-Type': 'application/json'
                }
            });

            // make a post request to the backend
            // if the current product id is -1, receive the new product id
            if (isNewUser) {
                this.id = response.data.id;// receive the new product id
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
     * delete this user from the databasenew User(4, "example4@company.com", "Full Name 4", "Viewer", "4 February 2023")
     */
    async delDatabase() {
        try {
            const isNewUser = this.id < 0;
            if (isNewUser) {
                return false;
            }
            // make a delete request to the backend
            await axios.delete(`/api/users/${this.id}`, {
                headers: {
                    "Authorization": getKey()
                }
            });
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
                headers: {
                    "Authorization": getKey()
                }
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
