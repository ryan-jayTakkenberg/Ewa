package app.controllers;

import app.util.HashUtil;
import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.jwt.JWToken;
import app.models.User;
import app.repositories.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserJPARepository userRepo;

    @GetMapping
    private List<User> getUsers(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo.isAdmin()) return userRepo.findAll();
        return List.of(userRepo.findById(jwtInfo.getId()));
    }

    @GetMapping("/{id}")
    private User getUserById(@PathVariable long id) {
        return userRepo.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private User postUser(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody User user) {
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create an user");

        if (user.getPassword() != null) {
            // Hash given password
            String hashedPassword = HashUtil.hash(user.getPassword());
            user.setPassword(hashedPassword);
        }

        boolean isNewUser = user.getId() == -1;
        if (isNewUser) return userRepo.save(user);
        else {
            User editedUser = this.userRepo.findById(user.getId());
            if (editedUser != null) {
                // Save edited user
                return this.userRepo.save(editedUser);
            }
        }
        return this.userRepo.save(user);
    }

    @DeleteMapping("/{id}")
    private User deleteProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {

        if (jwtInfo == null) throw new ForbiddenException("No token provided"); // Check if the jwt is provided
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to delete an user"); // Check if the user is admin

        if (id == null) throw new BadRequestException("No valid ID provided for product"); // Check if id is not null

        User user = userRepo.findById(id); // Find user by id
        if (user == null) throw new BadRequestException("No product found for such id"); // Check if user is found

        return userRepo.delete(user);
    }

}
