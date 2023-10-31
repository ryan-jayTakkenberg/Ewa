package app.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Teams {
    private int id;
    private String name;
    private List<User> users;
    private String warehouse;
    private String project;

    public Teams(int id, String name, String warehouse, String project, List<User> users) {
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
        this.project = project;
        this.users = users != null ? new ArrayList<>(users) : new ArrayList<>();
    }

    public Teams clone() {
        return new Teams(this.id, this.name, this.warehouse, this.project, new ArrayList<>(this.users));
    }

    public boolean equals(Teams other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        return this.id == other.id &&
                Objects.equals(this.name, other.name) &&
                Objects.equals(this.warehouse, other.warehouse) &&
                Objects.equals(this.project, other.project) &&
                this.users.equals(other.users);
    }

    public boolean putDatabase() {
        try {
            boolean isNewTeam = this.id < 0;
            // TODO: Implement the logic to put the team into the database
            if (isNewTeam) {
                this.id = Teams.teams.size(); // receive the new team id
                Teams.teams.add(this);
            } else {
                int index = -1;
                for (int i = 0; i < Teams.teams.size(); i++) {
                    if (Teams.teams.get(i).id == this.id) {
                        index = i;
                        break;
                    }
                }
                if (index >= 0) {
                    Teams.teams.set(index, this);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delDatabase() {
        try {
            boolean isNewTeam = this.id < 0;
            if (isNewTeam) {
                return false;
            }
            // TODO: Implement the logic to delete the team from the database
            Teams.teams.removeIf(team -> team.id == this.id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


        public static List<Teams> getDatabase() {
            // TODO: Implement the logic to get all teams from the database
            //  Update "teams" with the response
            return Arrays.asList(
                    new Teams(232, "TeamWest", "Amsterdam West", "Hva", Arrays.asList(new User("Persoon 1 "))),
                    new Teams(235, "SolarPaneel", "Amsterdam West", "Hva", Arrays.asList(new User("Persoon 2 "))),
                    new Teams(237, "Solar2", "Amsterdam West", "Hva", Arrays.asList(new User("Persoon 3 ")))
            );
        }


        public static boolean fetching = true;
    public static List<Teams> teams = new ArrayList<Teams>();

    public static Teams createNewTeam(String name, List<User> users, String warehouse, String project) {
        return new Teams(-1, name, warehouse, project, users);
    }
}
