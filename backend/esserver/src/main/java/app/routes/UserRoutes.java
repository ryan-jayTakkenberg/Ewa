package app.routes;

import app.EsserverApplication;
import app.authentication.Credentials;
import app.authentication.Perms;
import app.database.DatabaseHelper;
import app.models.Product;
import app.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = EsserverApplication.CROSS_ORIGIN)
@RestController
@RequestMapping("/api/user")
public class UserRoutes {

    @GetMapping
    private List<User> getUsers(@RequestHeader("Authorization") String authorization) {
        Perms permission = Credentials.getPermissions(authorization);
        permission.canRead();
        return permission.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private User postUser(@RequestHeader("Authorization") String authorization, @RequestBody User user) {
        Perms permission = Credentials.getPermissions(authorization);
        DatabaseHelper.addUser(permission, user);
        return user;
    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    private void deleteProduct(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
//        Perms permission = Credentials.getPermissions(authorization);
//        permission.canDelete();
//        DatabaseHelper.deleteProduct(id);
//    }

}
