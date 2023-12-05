package app.repository;

import app.models.Team;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamRepositoryMock implements TeamRepository {

    private final List<Team> teams = new ArrayList<>();

    public static int teamsIdCount = 7;

    // Constructor to add sample data on initialization




    @Override
    public List<Team> findAll() {
        return teams;
    }

    @Override
    public Team findById(Long id) {
        return teams.stream()
                .filter(team -> team.getId() == (id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Team save(Team team) {
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
