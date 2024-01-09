package app.controllers;

import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.Team;
import app.models.Warehouse;
import app.repositories.TeamJPARepository;
import app.repositories.WarehouseJPARepository;
import app.util.JsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = EsserverApplication.CROSS_ORIGIN)
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamJPARepository teamRepository;
    private final WarehouseJPARepository warehouseRepository;


    public TeamController(TeamJPARepository teamRepository, WarehouseJPARepository warehouseRepository) {
        this.teamRepository = teamRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping
    private List<Team> getTeams(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {

        return teamRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Team addNewTeam(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);

        String name = jsonBuilder.getStringFromField("name");
        // You cannot post a warehouse object, so only supply the id
        long warehouseId = jsonBuilder.getLongFromField("warehouseId");

        // Find the warehouse object from the supplied id
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null) {
            throw new NotFoundException("Warehouse not found for id: " + warehouseId);
        }

        // Now create the team object with the required warehouse object
        Team team = new Team(name, warehouse);
        return teamRepository.save(team);
    }

    @GetMapping("/{id}")
    public Team getTeamById(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,@PathVariable long id) {

        Team team = teamRepository.findById(id);

        if (team != null) {
            return team;
        } else {
            throw new NotFoundException("Team not found with ID: " + id);
        }
    }


    @PutMapping("/{id}")
    public Team updateTeam(
            @RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo,
            @PathVariable long id,
            @RequestBody JsonNode json) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create a user");
        }

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);

        String name = jsonBuilder.getStringFromField("name");
        // You cannot post a warehouse object, so only supply the id
        long warehouseId = jsonBuilder.getLongFromField("warehouseId");

        // Find the warehouse object from the supplied id
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null) {
            throw new NotFoundException("Warehouse not found for id: " + warehouseId);
        }

        // Now create the team object with the required warehouse object
        Team team = new Team(name, warehouse);

        int teamId = (int) id;

        team.setId(teamId);

        return teamRepository.save(team);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Team> deleteTeam(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable long id) {
        Team teamToDelete = teamRepository.findById(id);
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to delete a team");
        }
        if (teamToDelete != null) {

            teamRepository.delete(teamToDelete);
            return ResponseEntity.ok(teamToDelete);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/projects/{teamId}")
    public ResponseEntity<List<Team>> getProjectCountByTeam(@PathVariable long teamId) {
        List<Team> projectCount = teamRepository.findByQuery("TEAM_ID_COUNT", teamId);
        return ResponseEntity.ok(projectCount);
    }
}
