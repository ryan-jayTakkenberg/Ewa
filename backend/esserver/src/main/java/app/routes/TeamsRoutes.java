package app.routes;

import app.EsserverApplication;
import app.models.Teams;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
