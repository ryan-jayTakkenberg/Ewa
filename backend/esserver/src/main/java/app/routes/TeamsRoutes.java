package app.routes;

import app.authentication.Credentials;
import app.EsserverApplication;
import app.authentication.Perms;
import app.database.DatabaseHelper;
import app.models.Product;
import app.models.Teams;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = EsserverApplication.CROSS_ORIGIN)
@RestController
@RequestMapping("/api/teams")
public class TeamsRoutes {

    @GetMapping
    private List<Teams> getProducts() {
        return Teams.getDatabase();
    }



}
