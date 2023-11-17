package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.jwt.JWToken;
import app.models.Product;
import app.repositories.ProductJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

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
    private ProductJPARepository productRepo;

    @GetMapping
    private List<Product> getProducts(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }

        return productRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product postProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Product product) {
        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create a product");
        }

        return productRepo.save(product);
    }

    @DeleteMapping("/{id}")
    private Product deleteProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        if (jwtInfo == null) {
            throw new ForbiddenException("No token provided");
        }
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to remove a product");
        }

        if (id == null) {
            throw new BadRequestException("No valid ID provided for product");
        }

        Product product = productRepo.findById(id);
        if (product == null) {
            throw new BadRequestException("No product found for such id");
        }

        return productRepo.delete(product);
    }

}
