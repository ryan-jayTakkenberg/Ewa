package app.controllers;

import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.Util;
import app.exceptions.BadRequestException;
import app.models.UserModel;
import app.repositories.UserJPARepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private static JWTConfig jwtConfig = JWTConfig.getInstance();

    @Autowired
    private UserJPARepository userRepo;

    @PostMapping(path = "/login")
    public ResponseEntity<UserModel> authenticateAccount(@RequestBody ObjectNode signInInfo) {
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

        // Issue a token for the account, valid for some time
        JWToken jwToken = new JWToken(account.getName(), account.getId(), account.getPermissionLevel());
        String tokenString = jwToken.encode(jwtConfig.getIssuer(), jwtConfig.getPassphrase(), jwtConfig.getExpiration());
        return ResponseEntity.accepted()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString)
                .body(account);
    }

    @GetMapping(path = "/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authenticateAccount() {
        // Returns status 204 (No content) when JWT is valid, otherwise returns status 401 (Unauthorized) when JWT is invalid
    }
}