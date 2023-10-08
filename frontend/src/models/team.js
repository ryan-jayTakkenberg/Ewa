export default class Team {
    constructor(id, name, users = []) {
        this.id = id;
        this.name = name;
        this.users = users;
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

    clone() {
        return new Team(this.id, this.name, [...this.users]);
    }

    /**
     * put this team into the database
     */
    async putDatabase() {
        try {
            const isNewTeam = this.id < 0;
            // TODO make a post request to the backend
            //  if the current team id is -1, receive the new team id
            if (isNewTeam) {
                this.id = Team.teams.length;// receive the new team id
                Team.teams.push(this);
            } else {
                Team.teams[Team.teams.findIndex(t => t.id === this.id)] = this;
            }
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * delete this team from the database
     */
    async delDatabase() {
        try {
            const isNewTeam = this.id < 0;
            if (isNewTeam) {
                return false;
            }
            // TODO make a delete request to the backend
            Team.teams = Team.teams.filter(t => t.id !== this.id);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * get all the teams from the database
     */
    static async getDatabase() {
        try {
            this.fetching = true;
            // TODO make a get request to the backend
            //  update "teams" with the response
            await new Promise((resolve) => {
                setTimeout(resolve, 1000); // Sleep for 1 second (1000 milliseconds)
            });
            return [new Team(0, 'team1'), new Team(1, 'team2'), new Team(2, 'team3')];
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = false;
    static teams = [];
    static template = new Team(-1, null);
}