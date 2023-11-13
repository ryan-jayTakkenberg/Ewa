package app.authentication;

import app.exceptions.BadRequestException;
import app.exceptions.ForbiddenException;
import app.models.UserModel;
import app.repositories.UserJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {

    @Autowired
    private UserJPARepository userRepo;

    @Transactional
    public UserModel getUser(String authorization) {
        try {
            UUID uuid = UUID.fromString(authorization);

            for (UserModel user : userRepo.findAll()) {
                if (uuid.equals(user.getUuid())) {
                    return user;
                }
            }

            throw new ForbiddenException("Invalid credentials");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid authorization header");
        }
    }

    public void validate(String authorization) {
        getUser(authorization);
    }

    public PermissionLevel getPermissions(String authorization) {
        return getUser(authorization).getPermissionLevel();
    }

    public void checkForAdmin(String authorization) {
        if (getPermissions(authorization) != PermissionLevel.ADMIN) {
            throw new ForbiddenException("Invalid permissions");
        }
    }
}
