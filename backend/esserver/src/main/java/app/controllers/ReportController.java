package app.controllers;

import app.exceptions.BadRequestException;
import app.jwt.JWToken;
import app.models.Report;
import app.repositories.ReportJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/overview") // TODO update correct path
public class ReportController {

    /*
     * PRODUCT CONTROLLER
     * This controller class manages the CRUD (Create, Read, Update, Delete) operations for the 'Report' entity.
     *
     * Endpoints:
     * - GET /reports: Retrieves a list of all reports that belong to the logged-in user.
     * - POST /reports: Creates a new report.
     * - DELETE /reports/{id}: Removes a report by ID.
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
    private ReportJPARepository reportRepo;

    @GetMapping
    private List<Report> getReports(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {

        // TODO check logged in user id and fetch their respective reports

//        if (jwtInfo == null) {
//            throw new ForbiddenException("No token provided");
//        }

        return reportRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Report postReport(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Report report) {

        // TODO check logged in user id and post new report into their respective database

//        if (jwtInfo == null) {
//            throw new ForbiddenException("No token provided");
//        }
//        if (!jwtInfo.isAdmin()) {
//            throw new ForbiddenException("Admin role is required to create a product");
//        }

        return reportRepo.save(report);
    }

    @DeleteMapping("/{id}")
    private Report deleteReport(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {

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

        Report report = reportRepo.findById(id);
        if (report == null) {
            throw new BadRequestException("No report found with that id");
        }

        return reportRepo.delete(report);
    }

}
