package app.routes;

import app.authentication.AuthenticationService;
import app.authentication.PermissionLevel;
import app.exceptions.BadRequestException;
import app.models.UserModel;
import app.repositories.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRoutes {

    @Autowired
    private UserJPARepository userRepo;
    @Autowired
    private AuthenticationService credentials;

    @GetMapping
    private List<UserModel> getUsers(@RequestHeader("Authorization") String authorization) {
        UserModel user = credentials.getUser(authorization);
        if (user.getPermissionLevel() == PermissionLevel.ADMIN) {
            return userRepo.findAll();
        }
        return List.of(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private UserModel postUser(@RequestHeader("Authorization") String authorization, @RequestBody UserModel user) {
        credentials.checkForAdmin(authorization);
        return userRepo.save(user);
    }

    @DeleteMapping("/{id}")
    private UserModel deleteProduct(@RequestHeader("Authorization") String authorization, @PathVariable Integer id) {
        credentials.checkForAdmin(authorization);
        if (id == null) {
            throw new BadRequestException("No valid ID provided for product");
        }

        UserModel user = userRepo.findById(id);
        if (user == null) {
            throw new BadRequestException("No product found for such id");
        }

        return userRepo.delete(user);
    }

}
