package app.routes;

import app.authentication.AuthenticationService;
import app.exceptions.BadRequestException;
import app.models.Product;
import app.repositories.ProductJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductRoutes {

    /*
     * BACKEND: ROUTES
     * Autowire your JPA Repository (here ProductJPARepository), create one if you haven't already.
     * The JPA Repository will make all connections to the database, such as 'save()'.
     * You can use this repository for all the endpoints.
     *
     * Autowire the AuthenticationService.
     * Let's name the AuthenticationService 'credentials'.
     * Now we can access the user who made the request to the backend using:
     * 'credentials.getUser(authorization)', where 'authorization' is the authorization string in the header.
     * There are more methods such as 'credentials.mustBeAdmin(authorization)'.
     * It shouldn't be necessary to change the AuthenticationService.
     */

    @Autowired
    private ProductJPARepository productRepo;
    @Autowired
    private AuthenticationService credentials;

    @GetMapping
    private List<Product> getProducts(@RequestHeader("Authorization") String authorization) {
        credentials.validate(authorization);
        return productRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product postProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product) {
        credentials.mustBeAdmin(authorization);
        return productRepo.save(product);
    }

    @DeleteMapping("/{id}")
    private Product deleteProduct(@RequestHeader("Authorization") String authorization, @PathVariable Integer id) {
        credentials.mustBeAdmin(authorization);
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
