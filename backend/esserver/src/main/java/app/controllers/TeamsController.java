package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.Teams;
import app.repositories.TeamJPARepository;
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

    private final TeamJPARepository teamRepository;

    public TeamsController(TeamJPARepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    private List<Teams> getTeams(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo.isAdmin()) {
            return teamRepository.findAll();
        }

        return List.of(teamRepository.findById(jwtInfo.getId()));
    }

    @PostMapping
    public ResponseEntity<Teams> addNewTeam(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@RequestBody Teams team) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create an user");
        }

        if (team == null) {
            return ResponseEntity.badRequest().build();
        }

        Teams addedTeam = teamRepository.save(team);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedTeam.getId())
                .toUri();

        return ResponseEntity.created(location).body(addedTeam);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Teams> updateWarehouse(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@PathVariable long id, @RequestBody Teams updatedTeams) {
        Teams existingTeam = teamRepository.findById(id);
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to delete an user");
        }
        if (existingTeam != null) {
            if (id != updatedTeams.getId()) {
                throw new BadRequestException("ID in path does not match ID in request.");
            }

            updatedTeams.setId((int) id);
            Teams savedTeams = teamRepository.save(updatedTeams);
            return ResponseEntity.ok(savedTeams);
        } else {
            throw new NotFoundException("Warehouse not found with ID: " + id);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Teams> deleteTeam(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@PathVariable long id) {
        Teams teamToDelete = teamRepository.findById(id);
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to delete an user");
        }
        if (teamToDelete != null) {
            teamRepository.delete(teamToDelete);
            return ResponseEntity.ok(teamToDelete);
        }


        return null;
    }
}
