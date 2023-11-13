package app.authentication;

import app.exceptions.ForbiddenException;
import app.models.Product;
import app.models.User;
import app.repositories.ProductJPARepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Permissions are used to restrict the rights of a user to certain actions and results.
 * Each user has their own set of permissions, making each Permission object unique to that user.
 * Permissions can be used to limit the visibility of items (such as warehouses).
 * Permissions can also be used to restrict actions such as creating, updating or deleting items (such as products).
 */
public class Perms {
    private final User user;

    public Perms(User user) {
        this.user = user;
    }

    /*
     Permission actions
     */

    public List<Product> getProducts() {
        return Product.list;
    }

    public List<User> getUsers() {
        if (user.getPermissionLevel() == PermissionLevel.ADMIN) {
            return User.list;
        }
        return List.of(user);
    }

    /*
     CRUD actions
     */

    public void canCreate() {
        if (user.getPermissionLevel() != PermissionLevel.ADMIN) {
            throw new ForbiddenException("You are not allowed to create");
        }
    }

    public void canRead() {
        if (user.getPermissionLevel() != PermissionLevel.ADMIN && user.getPermissionLevel() != PermissionLevel.VIEWER) {
            throw new ForbiddenException("You are not allowed to read");
        }
    }

    public void canUpdate() {
        if (user.getPermissionLevel() != PermissionLevel.ADMIN) {
            throw new ForbiddenException("You are not allowed to update");
        }
    }

    public void canDelete() {
        if (user.getPermissionLevel() != PermissionLevel.ADMIN) {
            throw new ForbiddenException("You are not allowed to delete");
        }
    }
}
