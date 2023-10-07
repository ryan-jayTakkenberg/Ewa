export default class Team {
    constructor(name) {
        this.name = name;
        this.users = [];
    }

    equals(other) {
        if (!other) {
            return false;
        }
        return this.name === other.name && this.users.toString() === other.users.toString();
    }

    clone() {
        let team = new Team(this.name);
        team.users = [...this.users];
        return team;
    }

    static createNewTeam(name) {
        return new Team(name);
    }

    static teams = [Team.createNewTeam('team1'), Team.createNewTeam('team2'), Team.createNewTeam('team3')];
    static template = new Team(null);
}