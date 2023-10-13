import Team from "@/models/team";

export default class User {
   constructor(id, email, name, userRole, dateLastLoggedIn, password) {
       this.id = id;
       this.email = email;
       this.name = name;
       this.userRole = userRole;
       this.lastLoggedIn = dateLastLoggedIn;
       this.password = password;
   }

    static copyConstructor(user) {
        if (user === null || user === undefined) return null;
        return Object.assign(new User(user.id, user.email, user.name, user.userRole, user.lastLoggedIn), user);
    }


   getTeams() {
       return Team.teams.filter(team => team.users.includes(this));
   }

   equals(other) {
       return other && this.name === other.name;
   }

    static createNewUser(name) {
        return new User(-1, name, );
    }

    /**
     * put this user into the database
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
                User.users[User.users.findIndex(u => u.id === this.id)] = this;
            }
            return true;
        } catch (e) {
            return false;
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
            // TODO make a delete request to the backend
            User.users = User.users.filter(u => u.id !== this.id);
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
            await new Promise((resolve) => {
                setTimeout(resolve, 1000); // Sleep for 1 second (1000 milliseconds)
            });
            return [User.createNewUser('user1', 'admin'), User.createNewUser('user2'), User.createNewUser('user3', 'admin')];
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = false;
    static users = [];
    static template = new User(-1, null);
}
