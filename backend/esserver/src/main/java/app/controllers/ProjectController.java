package app.controllers;

import app.exceptions.BadRequestException;
import app.jwt.JWToken;
import app.models.Project;
import app.repositories.ProjectJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    /*
     * Project CONTROLLER
     * This controller class manages the CRUD (Create, Read, Update, Delete) operations for the 'Project' entity.
     *
     * Endpoints:
     * - GET /projects: Retrieves a list of all reports that belong to the logged-in user.
     * - POST /projects: Creates a new report.
     * - DELETE /projects/{id}: Removes a report by ID.
     *
     * Authorization:
     * - All endpoints require a valid JWT token, and a ForbiddenException is thrown if no token is provided.
     * - Admin-level token is required for creating and deleting products; otherwise, a ForbiddenException is thrown.
     *
     * Error Handling:
     * - Throws ForbiddenException for authentication and authorization issues.
     * - Throws BadRequestException for invalid or missing parameters.
     *
     * Dependencies:
     * - Autowired 'ProductJPARepository' for database interaction.
     * - Utilizes 'JWToken' for extracting JWT information from the request attributes.
     *
     * Note:
     * - The controller assumes a REST structure and adheres to HTTP status codes.
     * - Ensure that the 'ProductJPARepository' is properly configured for database operations.
     * - POST can also be used to UPDATE (edit) the entity
     */

    @Autowired
    private ProjectJPARepository projectsRepo;

    @GetMapping
    private List<Project> getProjects(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {

        return projectsRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Project postProject(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Project project) {

        // TODO check logged in user id and post new report into their respective database

        return projectsRepo.save(project);
    }

    @DeleteMapping("/{id}")
    private Project deleteProject(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {

        // TODO delete report with given id

//        if (jwtInfo == null) {
//            throw new ForbiddenException("No token provided");
//        }
//        if (!jwtInfo.isAdmin()) {
//            throw new ForbiddenException("Admin role is required to remove a product");
//        }

        if (id == null) {
            throw new BadRequestException("No valid ID provided for report");
        }

        Project project = projectsRepo.findById(id);
        if (project == null) {
            throw new BadRequestException("No report found with that id");
        }

        return projectsRepo.delete(project);
    }

}