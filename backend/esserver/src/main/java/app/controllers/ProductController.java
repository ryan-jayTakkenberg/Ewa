package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.exceptions.NotFoundException;
import app.jwt.JWToken;
import app.models.Product;
import app.repositories.ProductJPARepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PRODUCT CONTROLLER
 * <br />This controller class manages the CRUD (Create, Read, Update, Delete) operations for the 'Product' entity.
 * @apiNote Endpoints:
 * <br />- GET /product: Retrieves a list of all products. Requires a valid JWT token for authentication.
 * <br />- POST /product: Creates a new product. Requires an admin-level JWT token for authorization.
 * <br />- DELETE /product/{id}: Removes a product by ID. Requires an admin-level JWT token for authorization.
 * <br />
 * <br />Authorization:
 * <br />- All endpoints require a valid JWT token, and a ForbiddenException is thrown if no token is provided.
 * <br />- Admin-level token is required for creating and deleting products; otherwise, a ForbiddenException is thrown.
 * <br />
 * <br />Error Handling:
 * <br />- Throws ForbiddenException for authentication and authorization issues.
 * <br />- Throws BadRequestException for invalid or missing parameters.
 * <br />
 * <br />Dependencies:
 * <br />- Autowired 'ProductJPARepository' for database interaction.
 * <br />- Utilizes 'JWToken' for extracting JWT information from the request attributes.
 * <br />
 * <br />Note:
 * <br />- The controller assumes a REST structure and adheres to HTTP status codes.
 * <br />- Ensure that the 'ProductJPARepository' is properly configured for database operations.
 * <br />- The POST /product can also be used to edit (usually PUT) the product
 * @author Leon
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductJPARepository productRepo;

    public ProductController(ProductJPARepository productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * Getting all the products
     * @return list of products
     */
    @GetMapping
    private List<Product> getProducts() {
        return productRepo.findAll();
    }

    /**
     * Get a specific product
     * @return The product for the specified id
     */
    @GetMapping("/{id}")
    private Product getProduct(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("Missing field: 'id'");
        }

        Product product = productRepo.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found for id: " + id);
        }

        return product;
    }

    /**
     * Add a product to the database
     * @param jwtInfo the json web token
     * @param productInfo the product to add or edit
     * @return the product if it was added successfully
     * @apiNote requires admin permission
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product postProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Product productInfo) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create a product");
        }
        if (productRepo.findById(productInfo.getId()) != null) {
            throw new BadRequestException("Product already exists for id: " + productInfo.getId());
        }

        return productRepo.save(productInfo);
    }

    /**
     * Edit a product to the database
     * @param jwtInfo the json web token
     * @param productInfo the product to add or edit
     * @return the product if it was edited successfully
     * @apiNote requires admin permission
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product putProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Product productInfo) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to edit a product");
        }
        if (productRepo.findById(productInfo.getId()) == null) {
            throw new NotFoundException("No product found for id: " + productInfo.getId());
        }

        return productRepo.save(productInfo);
    }

    /**
     * Deletes a product from the database
     * @param jwtInfo the json web token
     * @param id the product id
     * @return the deleted product
     * @apiNote requires admin permission
     */
    @DeleteMapping("/{id}")
    private Product deleteProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to remove a product");
        }

        if (id == null) {
            throw new BadRequestException("No valid ID provided for product");
        }

        Product productInfo = productRepo.findById(id);
        if (productInfo == null) {
            throw new BadRequestException("No product found for such ID");
        }

        return productRepo.delete(productInfo);
    }

}
