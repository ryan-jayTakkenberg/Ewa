package app.controllers;

import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.Util;
import app.exceptions.BadRequestException;
import app.models.UserModel;
import app.repositories.UserJPARepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private static final JWTConfig jwtConfig = JWTConfig.getInstance();

    private final UserJPARepository userRepo;

    public AuthenticationController(UserJPARepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserModel> authenticateAccount(@RequestBody ObjectNode signInInfo, HttpServletRequest request) {
        if (!signInInfo.has("username")) {
            throw new BadRequestException("Username is required");
        }
        if (!signInInfo.has("password")) {
            throw new BadRequestException("Password is required");
        }

        String username = signInInfo.get("username").asText();
        String password = signInInfo.get("password").asText();
        String hashedPassword = Util.hash(password);

        UserModel account = null;
        for (UserModel user : userRepo.findAll()) {
            if (user.getName().equals(username) && user.getPassword().equals(hashedPassword)) {
                user.setLastLogin(LocalDate.now());
                account = user;
                break;
            }
        }
        if (account == null) {
            throw new BadRequestException("Username and password are incorrect");
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        // Issue a token for the account, valid for some time
        JWToken jwToken = new JWToken(account.getId(), account.getPermissionLevel(), ip, jwtConfig.getExpiration());
        String tokenString = jwToken.encode(jwtConfig.getIssuer(), jwtConfig.getPassphrase());
        return ResponseEntity.accepted()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString)
                .body(account);
    }

    @GetMapping(path = "/status")
    public ResponseEntity<?> authenticateAccount() {
        // Returns status 204 (No content) when JWT is valid, otherwise returns status 401 (Unauthorized) when JWT is invalid
        return ResponseEntity.noContent().build();
    }
}