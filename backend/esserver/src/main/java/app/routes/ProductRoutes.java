package app.routes;

import app.authentication.Credentials;
import app.EsserverApplication;
import app.authentication.Perms;
import app.database.DatabaseHelper;
import app.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = EsserverApplication.CROSS_ORIGIN)
@RestController
@RequestMapping("/api/product")
public class ProductRoutes {

    @GetMapping
    private List<Product> getProducts(@RequestHeader("Authorization") String authorization) {
        Perms permission = Credentials.getPermissions(authorization);
        permission.canRead();
        return permission.getProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Product postProduct(@RequestHeader("Authorization") String authorization, @RequestBody Product product) {
        Perms permission = Credentials.getPermissions(authorization);
        DatabaseHelper.addProduct(permission, product);
        return product;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteProduct(@RequestHeader("Authorization") String authorization, @PathVariable Integer id) {
        Perms permission = Credentials.getPermissions(authorization);
        permission.canDelete();
        DatabaseHelper.deleteProduct(id);
    }

}
