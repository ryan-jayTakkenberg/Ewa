import Team from "@/models/team";

export default class User {
   constructor(name) {
       this.name = name;
   }

   getTeams() {
       return Team.teams.filter(team => team.users.includes(this));
   }

   equals(other) {
       if (!other) {
           return false;
       }
       return this.name === other.name;
   }

    static createNewUser(name) {
        return new User(name);
    }

    static users = [User.createNewUser('user1'), User.createNewUser('user2'), User.createNewUser('user3')];
    static template = new User(null);
}