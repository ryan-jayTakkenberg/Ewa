package app.repository;

import app.models.Team;

import java.util.List;

public interface TeamRepository {

    List<Team> findAll();
    Team findById(Long id);
    Team save(Team team);
    void delete(Long id);


}
