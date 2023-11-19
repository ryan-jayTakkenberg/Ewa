package app.controllers;

import app.Util;
import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.jwt.JWToken;
import app.models.UserModel;
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
    private List<UserModel> getUsers(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        if (jwtInfo.isAdmin()) {
            return userRepo.findAll();
        }

        return List.of(userRepo.findById(jwtInfo.getId()));
    }

    @GetMapping("/{id}")
    private UserModel getUserById(@PathVariable long id) {
        return userRepo.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private UserModel postUser(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody UserModel user) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to create an user");
        }

        if (user.getPassword() != null) {
            // Hash given password
            String hashedPassword = Util.hash(user.getPassword());
            user.setPassword(hashedPassword);
        }

        boolean isEditing = user.getId() > 0;
        if (isEditing) {
            UserModel currentUser = this.userRepo.findById(user.getId());
            // TODO
        }

        return userRepo.save(user);
    }

    @DeleteMapping("/{id}")
    private UserModel deleteProduct(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        if (!jwtInfo.isAdmin()) {
            throw new ForbiddenException("Admin role is required to delete an user");
        }

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