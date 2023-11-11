package app.routes;

import app.EsserverApplication;
import app.models.Teams;
import app.repository.TeamsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = EsserverApplication.CROSS_ORIGIN)
@RestController
@RequestMapping("/api/teams")
public class TeamsRoutes {

    private final TeamsRepository teamsRepository;

    public TeamsRoutes(TeamsRepository teamsRepository) {
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
