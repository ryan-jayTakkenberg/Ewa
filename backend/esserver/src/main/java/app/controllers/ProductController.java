package app.controllers;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
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

    private final ProductJPARepository productInfoRepo;
    private final ProductJPARepository productRepo;

    public ProductController(ProductJPARepository productInfoRepo, ProductJPARepository productRepo) {
        this.productInfoRepo = productInfoRepo;
        this.productRepo = productRepo;
    }

    /**
     * Getting all the products
     * @return list of products
     */
    @GetMapping
    private List<Product> getProducts() {
        return productInfoRepo.findAll();
    }

    /**
     * Add (or edit) a product to the database
     * @param jwtInfo the json web token
     * @param productInfo the product to add or edit
     * @return the product if it was added or edited successfully
     * @apiNote requires admin permission
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product postProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody Product productInfo) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create a product");
        }

        return productInfoRepo.save(productInfo);
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

        Product productInfo = productInfoRepo.findById(id);
        if (productInfo == null) {
            throw new BadRequestException("No product found for such ID");
        }

        return productInfoRepo.delete(productInfo);
    }

}
