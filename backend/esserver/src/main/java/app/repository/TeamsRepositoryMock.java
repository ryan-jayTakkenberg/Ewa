package app.repository;

import app.models.Teams;

import app.models.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class TeamsRepositoryMock implements TeamsRepository {

    private final List<Teams> teams = new ArrayList<>();

    public static int teamsIdCount = 7;

    // Constructor to add sample data on initialization
    public TeamsRepositoryMock() {
        // Adding some sample users for each team

        for (int i = 1; i < 7; i++){
            teams.add(Teams.createSampleTeam(i));
        }
    }





    @Override
    public List<Teams> findAll() {
        return teams;
    }

    @Override
    public Teams findById(Long id) {
        return teams.stream()
                .filter(team -> team.getId() == (id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Teams save(Teams team) {
        if (team.getId() == 0) {
            // New team, assign an ID
            team.setId(teamsIdCount++);
            teams.add(team);
        } else {
            for (int i = 0; i <teams.size() ; i++) {
                if (teams.get(i).getId() == team.getId()){
                    teams.set(i,team);
                    break;
                }

            }
        }
        return team;
    }

    @Override
    public void delete(Long id) {
        teams.removeIf(team -> team.getId() == (id));
    }
}
