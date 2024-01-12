package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.jwt.JWToken;
import app.models.Report;
import app.repositories.ReportJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    /*
     * REPORT CONTROLLER
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
     * - Autowired 'ReportJPARepository' for database interaction.
     * - Utilizes 'JWToken' for extracting JWT information from the request attributes.
     *
     * Note:
     * - The controller assumes a REST structure and adheres to HTTP status codes.
     * - Ensure that the 'ReportsJPARepository' is properly configured for database operations.
     * - POST can also be used to UPDATE (edit) the entity
     */

    @Autowired
    private ReportJPARepository reportRepo;

    @GetMapping
    private List<Report> getReports(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {

        if (jwtInfo.getId() == 0){
            throw new ForbiddenException("Need JWT to access this information");
        }

        return reportRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Report postReport(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Report report) {

        if (jwtInfo.getId() == 0){
            throw new ForbiddenException("Need JWT to access this information");
        }

        if (report.getSenderId() == 0 || report.getReceiverId() == 0) {
            throw new BadRequestException("Invalid data in the report");
        }

        return reportRepo.save(report);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Report> deleteReport(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {

        Report reportToDelete = reportRepo.findById(id);

        if (jwtInfo.getId() == 0){
            throw new ForbiddenException("Need JWT to access this information");
        }

        if (reportToDelete != null) {
            reportRepo.delete(reportToDelete);
            return ResponseEntity.ok(reportToDelete);
        }

        return ResponseEntity.notFound().build();
    }

}