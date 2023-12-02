package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.Project;
import app.repositories.ProjectJPARepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectJPARepository projectsRepo;

    public ProjectController(ProjectJPARepository projectJPARepository){
     this.projectsRepo = projectJPARepository;
    }

    @GetMapping
    private List<Project> getProjects(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo.isAdmin()) {
            return projectsRepo.findAll();
        }
        return List.of(projectsRepo.findById(jwtInfo.getId()));
    }

    @GetMapping ("/{id}")
    private Project getProjectById(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable long id){
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to search for a specific search");
        }
        return projectsRepo.findById(id);
    }


    @PostMapping
    public ResponseEntity<Project> addNewProject(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Project project) {
        if (!jwtInfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to create a project");
        }

        if (project == null){
            return ResponseEntity.badRequest().build();
        }

        Project addedProject = projectsRepo.save(project);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedProject.getProjectId())
                .toUri();

        return ResponseEntity.created(location).body(addedProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtinfo, @PathVariable long id, @RequestBody Project updatedProject){
        Project existingProject = projectsRepo.findById(id);
        if (!jwtinfo.isAdmin()){
            throw new ForbiddenException("Admin role is required to alter a project");
        }

        if (existingProject != null){
            if (id != updatedProject.getProjectId()){
                throw new BadRequestException("ID in path does not match id in request");
            }

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