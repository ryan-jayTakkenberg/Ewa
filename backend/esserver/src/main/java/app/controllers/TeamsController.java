package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.models.Teams;
import app.repository.TeamsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

//@CrossOrigin(origins = EsserverApplication.CROSS_ORIGIN)
@RestController
@RequestMapping("/teams")
public class TeamsController {

    private final TeamsRepository teamsRepository;

    public TeamsController(TeamsRepository teamsRepository) {
        this.teamsRepository = teamsRepository;
    }

    @GetMapping
    private List<Teams> getTeams() {
        return teamsRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Teams> addNewTeam(@RequestBody Teams team) {
        if (team == null) {
            return ResponseEntity.badRequest().build();
        }

        Teams addedTeam = teamsRepository.save(team);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedTeam.getId())
                .toUri();

        return ResponseEntity.created(location).body(addedTeam);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Teams> updateWarehouse(@PathVariable long id, @RequestBody Teams updatedTeams) {
        Teams existingTeam = teamsRepository.findById(id);
        if (existingTeam != null) {
            if (id != updatedTeams.getId()) {
                throw new BadRequestException("ID in path does not match ID in request.");
            }

            updatedTeams.setId((int) id);
            Teams savedTeams = teamsRepository.save(updatedTeams);
            return ResponseEntity.ok(savedTeams);
        } else {
            throw new NotFoundException("Warehouse not found with ID: " + id);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Teams> deleteTeam(@PathVariable long id) {
        Teams teamToDelete = teamsRepository.findById(id);
        if (teamToDelete != null) {
            teamsRepository.delete(id);
            return ResponseEntity.ok(teamToDelete);
        }


        return null;
    }
}
