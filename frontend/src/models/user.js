import Team from "@/models/team";

export default class User {

    static PermissionLevel = {
        VIEWER: "Viewer",
        ADMIN: "Admin",
    }

   constructor(id, email, name, permissionLevel, dateLastLoggedIn, password) {
       this.id = id;
       this.email = email;
       this.name = name;
       this.permissionLevel = permissionLevel;
       this.lastLoggedIn = dateLastLoggedIn;
       this.password = password;
   }

    clone() {
        return new User(this.id, this.email, this.name, this.permissionLevel, this.lastLoggedIn, this.password);
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
     * put this user into the database
     * will add a new user to the database if no user exists
     * will override the existing user with the new user if the user already exists
     */
    async putDatabase() {
        try {
            const isNewUser = this.id < 0;
            // TODO make a post request to the backend
            //  if the current user id is -1, receive the new user id
            if (isNewUser) {
                this.id = 0;// receive the new user id
                User.users.push(this);
            } else {
                User.users[User.users.findIndex(o => o.id === this.id)] = this;
            }
            return true;
        } catch (e) {
            return false;
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
            // TODO make a delete request to the backend
            User.users = User.users.filter(o => o.id !== this.id);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * get all the users from the database
     */
    static async getDatabase() {
        try {
            this.fetching = true;
            // TODO make a get request to the backend
            //  update "users" with the response
            return [new User(1, "example1@company.com", "Full Name 1", "Admin", "1 February 2023", "1234"), new User(2, "example2@company.com", "Full Name 2", "Viewer", "2 February 2023", "1234"), new User(3, "example3@company.com", "Full Name 3", "Admin", "3 February 2023", "1234"), new User(4, "example4@company.com", "Full Name 4", "Viewer", "4 February 2023", "1234") ];
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static users = [];
}
