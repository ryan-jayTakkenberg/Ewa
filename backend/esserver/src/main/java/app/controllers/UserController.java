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

    /**
     * Get a list of users based on the provided JWT token.
     *
     * @param jwtInfo the JSON Web Token
     * @return a list of users if the requester is an admin; otherwise, return the user corresponding to the JWT token.
     */
    @GetMapping
    private List<User> getUsers(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin else return only user itself
        if (jwtInfo.isAdmin()) return userRepo.findAll();
        else return List.of(userRepo.findById(jwtInfo.getId()));
    }
//    /**
//     * Get a user by ID.
//     * @param id the ID of the user to retrieve
//     * @return the user with the specified ID
//     */
//    @GetMapping("/{id}")
//    private User getUserById(@PathVariable long id) {
//        return userRepo.findById(id);
//    }

    /**
     * Create a new user or edit an existing user in the database.
     *
     * @param jwtInfo the JSON Web Token
     * @param user    the user to create or edit
     * @return the created or edited user
     * @throws ForbiddenException if the requester is not an admin
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private User postUser(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody User user) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create or edit a user");

        // Hash given password
        if (user.getPassword() != null) {
            String hashedPassword = HashUtil.hash(user.getPassword());
            user.setPassword(hashedPassword);
        }
        // Save user
        return userRepo.save(user);
    }

    /**
     * Edit a user in the database
     *
     * @param jwtInfo  the json web token
     * @param userInfo the user to add or edit
     * @return the user if it was edited successfully
     * @apiNote requires admin permission
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    private User putUser(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody User userInfo) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to edit a user");
        // Save user
        return userRepo.save(userInfo);
    }

    @DeleteMapping("/{id}")
    private User deleteProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to delete an user");
        // Check if id is not null
        if (id == null) throw new BadRequestException("No valid ID provided for user");
        // Find user by id
        User user = userRepo.findById(id);
        // Check if user is found
        if (user == null) throw new BadRequestException("No user found for id");
        // Delete user
        return userRepo.delete(user);
    }

}
