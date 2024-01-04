package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.Project;
import app.models.Team;
import app.repositories.ProjectJPARepository;
import app.repositories.TeamJPARepository;
import app.util.JsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectJPARepository projectsRepo;

    private final TeamJPARepository teamsRepo;

    public ProjectController(ProjectJPARepository projectJPARepository, TeamJPARepository teamJPARepository){
     this.projectsRepo = projectJPARepository;
     this.teamsRepo = teamJPARepository;
    }

    @GetMapping
    private List<Project> getProjects(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        return projectsRepo.findAll();
    }

    @GetMapping ("/{id}")
    private Project getProjectById(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable long id){
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to search for a specific search");
        }
        return projectsRepo.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project addNewProject(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create a project");
        }

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);

        int projectId = jsonBuilder.getIntFromField("projectId");
        String name = jsonBuilder.getStringFromField("projectName");
        String clientName = jsonBuilder.getStringFromField("clientName");
        String installDate = jsonBuilder.getStringFromField("installDate");
        String notes = jsonBuilder.getStringFromField("notes");
        long teamId = jsonBuilder.getLongFromField("teamId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate installDateToLocalDate = LocalDate.parse(installDate, formatter);

        Team team = teamsRepo.findById(teamId);
        if (team == null){
            throw new NotFoundException("Team not found for id: " + teamId);
        }

        return projectsRepo.save(new Project(projectId, name, clientName, installDateToLocalDate, notes, team));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtinfo, @PathVariable long id, @RequestBody JsonNode json){
        Project existingProject = projectsRepo.findById(id);
        if (!jwtinfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to alter a project");
        }

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);

        int projectId = jsonBuilder.getIntFromField("projectId");
        String name = jsonBuilder.getStringFromField("projectName");
        String clientName = jsonBuilder.getStringFromField("clientName");
        String installDate = jsonBuilder.getStringFromField("installDate");
        String notes = jsonBuilder.getStringFromField("notes");
        long teamId = jsonBuilder.getLongFromField("teamId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate installDateToLocalDate = LocalDate.parse(installDate, formatter);

        Team team = teamsRepo.findById(teamId);

        if (existingProject != null){
            if (id != projectId){
                throw new BadRequestException("ID in path does not match id in request");
            }

            Project updatedProject = new Project(projectId, name, clientName, installDateToLocalDate, notes, team);

            Project savedProject = projectsRepo.save(updatedProject);
            return ResponseEntity.ok(savedProject);
        } else {
            throw new NotFoundException("Warehouse not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable long id) {
        Project projectToDelete = projectsRepo.findById(id);
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to delete a project");
        }
        if (projectToDelete != null){
            projectsRepo.delete(projectToDelete);
            return ResponseEntity.ok(projectToDelete);
        }
        return null;
    }
}