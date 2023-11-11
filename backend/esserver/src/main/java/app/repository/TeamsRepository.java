package app.repository;

import app.models.Teams;

import java.util.List;

public interface TeamsRepository {

    List<Teams> findAll();
    Teams findById(Long id);
    Teams save(Teams team);
    void delete(Long id);


}
