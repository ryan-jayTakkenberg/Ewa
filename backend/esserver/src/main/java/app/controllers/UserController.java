package app.controllers;

import app.enums.PermissionLevel;
import app.models.Team;
import app.repositories.TeamJPARepository;
import app.util.HashUtil;
import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.jwt.JWToken;
import app.models.User;
import app.repositories.UserJPARepository;
import app.util.JsonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserJPARepository userRepo;

    @Autowired
    private TeamJPARepository teamRepo;

    /**
     * Get a list of users based on the provided JWT token.
     *
     * @param jwtInfo the JSON Web Token
     * @return a list of users if the requester is an admin
     * @throws ForbiddenException if no token provided
     * @throws ForbiddenException if the requester is not an admin
     * @apiNote requires admin permission
     */
    @GetMapping
    private List<User> getUsers(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
//        if (!jwtInfo.isAdmin()) {return List.of(userRepo.findById(jwtInfo.getId()));} // destroys report system

        // filter out the user where jwtInfo.getId() == user.id
        Long currentUserId = jwtInfo.getId();

        // Filter out the user where jwtInfo.getId() equals user.id
        return userRepo.findAll().stream()
                .filter(user -> !currentUserId.equals(user.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private User getUserById(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {

        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        if (id == null) throw new BadRequestException("No valid ID provided for user");
        return userRepo.findById(id);
    }

    /**
     * Create a new user or edit an existing user in the database.
     *
     * @param jwtInfo the JSON Web Token
     * @param user    the user to create
     * @return the created user if created successfully
     * @throws ForbiddenException if no token provided
     * @throws ForbiddenException if the requester is not an admin
     * @throws BadRequestException if already existing user found for id
     * @apiNote requires admin permission
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    private User postUser(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to create or edit a user");

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);
        PermissionLevel permissionLevel = PermissionLevel.valueOf(jsonBuilder.getStringFromField("permissionLevel"));
        String name = jsonBuilder.getStringFromField("name");
        String email = jsonBuilder.getStringFromField("email");
        String password = jsonBuilder.getStringFromField("password");
        long teamId = jsonBuilder.getLongFromField("teamId");
        Team team = teamRepo.findById(teamId);

        // Save user
        User user = new User(permissionLevel, name, email, null, password, team);
        return userRepo.save(user);
    }

    /**
     * Update a user in the database
     *
     * @param jwtInfo  the json web token
     * @param user the user to add or edit
     * @return the user if it was edited successfully
     * @throws ForbiddenException if no token provided
     * @throws ForbiddenException if the requester is not an admin
     * @throws BadRequestException if no existing user found for id
     * @apiNote requires admin permission
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    private User putUser(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @RequestBody JsonNode json) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to edit a user");

        JsonBuilder jsonBuilder = JsonBuilder.parse(json);
        PermissionLevel permissionLevel = PermissionLevel.valueOf(jsonBuilder.getStringFromField("permissionLevel"));
        String name = jsonBuilder.getStringFromField("name");
        String email = jsonBuilder.getStringFromField("email");
        String password = jsonBuilder.getStringFromField("password");
        long userId = jsonBuilder.getLongFromField("userId");
        long teamId = jsonBuilder.getLongFromField("teamId");
        Team team = teamRepo.findById(teamId);

        if (team != null && team.getUser() != null && team.getUser().getId() != userId) {
            throw new BadRequestException("This team already has an user");
        }


        // Check if user is found
        User user = userRepo.findById(userId);
        if (user == null) {
            throw new BadRequestException("No user found for ID" + userId);
        }

        // Save user
        user.setPermissionLevel(permissionLevel);
        user.setName(name);
        user.setEmail(email);
        user.setPasswordAndHash(password);
        user.setTeam(team);
        return userRepo.save(user);
    }

    /**
     * Deletes a user in the database
     *
     * @param jwtInfo  the json web token
     * @param id the user id to delete
     * @return the user if it was deleted successfully
     * @throws ForbiddenException if no token provided
     * @throws ForbiddenException if the requester is not an admin
     * @throws BadRequestException if id is not valid
     * @throws BadRequestException if no existing user found for id
     * @apiNote requires admin permission
     */
    @DeleteMapping("/{id}")
    private User deleteUser(@RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo, @PathVariable Long id) {
        // Check if the jwt is provided
        if (jwtInfo == null) throw new ForbiddenException("No token provided");
        // Check if the user is admin
        if (!jwtInfo.isAdmin()) throw new ForbiddenException("Admin role is required to delete an user");



        // Check if id is not null
        if (id == null) throw new BadRequestException("No valid ID provided for user");
        // Check if user is found
        User existingUser = userRepo.findById(id);
        if (existingUser == null) throw new BadRequestException("No user found for id");
        // Delete user
        return userRepo.delete(existingUser);
    }

}
