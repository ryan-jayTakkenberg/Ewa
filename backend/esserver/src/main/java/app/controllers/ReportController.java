package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.jwt.JWToken;
import app.models.Report;
import app.repositories.ReportJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/overview")
public class ReportController {

    /*
     * PRODUCT CONTROLLER
     * This controller class manages the CRUD (Create, Read, Update, Delete) operations for the 'Product' entity.
     *
     * Endpoints:
     * - GET /product: Retrieves a list of all products. Requires a valid JWT token for authentication.
     * - POST /product: Creates a new product. Requires an admin-level JWT token for authorization.
     * - DELETE /product/{id}: Removes a product by ID. Requires an admin-level JWT token for authorization.
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
     * - The POST /product can also be used to UPDATE (edit) the product
     */

    @Autowired
    private ReportJPARepository reportRepo;

    @GetMapping
    private List<Report> getReports(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }

        return reportRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Report postReport(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Report report) {
        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create a product");
        }

        return reportRepo.save(report);
    }

    @DeleteMapping("/{id}")
    private Report deleteReport(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to remove a product");
        }

        if (id == null) {
            throw new BadRequestException("No valid ID provided for product");
        }

        Report report = reportRepo.findById(id);
        if (report == null) {
            throw new BadRequestException("No product found for such id");
        }

        return reportRepo.delete(report);
    }

}
