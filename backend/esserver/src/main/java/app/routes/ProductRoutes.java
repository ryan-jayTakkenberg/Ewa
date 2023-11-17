package app.routes;

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
public class ProductRoutes {

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
