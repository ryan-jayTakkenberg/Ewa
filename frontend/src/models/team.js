import {getAPI} from "@/backend";

export default class Team {
    static idAutoIncrement = 12;
    constructor(id, name,warehouse) {
        this.id = id;
        this.name = name;
        this.warehouse = warehouse
        this.projectCount = 0;
    }


    static getAutoIncrementId() {
        return Team.idAutoIncrement++;
    }
    clone() {
        return new Team(this.id, this.name,this.warehouse);
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

    /**
     * put this team into the database
     * will add a new team to the database if no team exists
     * will override the existing team with the new team if the team already exists
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
                Team.teams[Team.teams.findIndex(o => o.id === this.id)] = this;
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
            Team.teams = Team.teams.filter(o => o.id !== this.id);
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
            const response = await getAPI("/api/teams");
            return response.data;
        } catch (e) {
            return [];
        } finally {
            this.fetching = false;
        }
    }

    static fetching = true;
    static teams = [];




}
