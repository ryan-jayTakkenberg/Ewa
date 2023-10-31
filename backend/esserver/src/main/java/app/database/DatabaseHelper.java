package app.database;

import app.authentication.Perms;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.models.Product;
import app.models.User;

import java.util.UUID;
import java.util.stream.IntStream;

public class DatabaseHelper {

    public static void addProduct(Perms permissions, Product product) {
        int index = IntStream.range(0, Product.list.size()).filter(i -> Product.list.get(i).getId() == product.getId()).findFirst().orElse(-1);
        if (index < 0) {
            permissions.canCreate();
            // Creating a new product
            long productId = (long) (Math.random() * Long.MAX_VALUE);// get this from auto increment in database
            product.setId(productId);
            Product.list.add(product);
            // TODO insert into database
        } else {
            permissions.canUpdate();
            // Editing an existing product
            Product.list.set(index, product);
            // TODO update database
        }
    }

    public static void deleteProduct(Long id) {
        if (id == null) {
            throw new BadRequestException("Invalid product ID");
        }
        int index = IntStream.range(0, Product.list.size()).filter(i -> Product.list.get(i).getId() == id).findFirst().orElseThrow(() -> new NotFoundException("No product found for such ID"));
        Product.list.remove(index);
    }

    public static void addUser(Perms permissions, User user) {
        int index = IntStream.range(0, User.list.size()).filter(i -> User.list.get(i).getId().equals(user.getId())).findFirst().orElse(-1);
        if (index < 0) {
            permissions.canCreate();
            // Creating a new product
            UUID userId = UUID.randomUUID(); // get this from auto increment in database
            user.setId(userId);
            User.list.add(user);
            // TODO insert into database
        } else {
            permissions.canUpdate();
            // Editing an existing product
            User.list.set(index, user);
            // TODO update database
        }
    }
}
