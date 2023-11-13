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
        credentials.checkForAdmin(authorization);
        return productRepo.save(product);
    }

    @DeleteMapping("/{id}")
    private Product deleteProduct(@RequestHeader("Authorization") String authorization, @PathVariable Integer id) {
        credentials.checkForAdmin(authorization);
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
