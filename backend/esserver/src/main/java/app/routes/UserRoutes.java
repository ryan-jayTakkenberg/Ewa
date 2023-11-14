package app.routes;

import app.Util;
import app.authentication.AuthenticationService;
import app.authentication.PermissionLevel;
import app.exceptions.BadRequestException;
import app.models.UserModel;
import app.repositories.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        credentials.mustBeAdmin(authorization);

        if (user.getPassword() != null) {
            // Hash given password
            String hashedPassword = Util.hash(user.getPassword());
            user.setPassword(hashedPassword);
        }

        boolean isEditing = user.getId() > 0;
        if (isEditing) {
            UserModel currentUser = this.userRepo.findById(user.getId());
            if (user.getUuid() == null) {
                user.setUuid(currentUser.getUuid());
            }
        } else {
            user.setUuid(UUID.randomUUID());
        }

        return userRepo.save(user);
    }

    @PostMapping("/login")
    private UserModel postLogin(@RequestBody UserModel login) {
        if (login.getName() == null) {
            throw new BadRequestException("Name is required");
        }
        if (login.getPassword() == null) {
            throw new BadRequestException("Password is required");
        }

        String username = login.getName();
        String hashedPassword = Util.hash(login.getPassword());

        for (UserModel user : userRepo.findAll()) {
            if (user.getName().equals(username) && user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }

        return login;
    }

    @DeleteMapping("/{id}")
    private UserModel deleteProduct(@RequestHeader("Authorization") String authorization, @PathVariable Integer id) {
        credentials.mustBeAdmin(authorization);
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
