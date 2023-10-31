package app.authentication;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.models.User;

import java.util.*;

public class Credentials {

    public static Perms getPermissions(String authorization) {
        try {
            UUID uuid = UUID.fromString(authorization);

            for (User user : User.list) {
                if (user.getId().equals(uuid)) {
                    return new Perms(user);
                }
            }

            throw new ForbiddenException("Invalid credentials");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid authorization header");
        }
    }
}
