package app;

import app.authentication.PermissionLevel;
import app.models.Product;
import app.models.UserModel;
import app.repositories.ProductJPARepository;
import app.repositories.UserJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductJPARepository productRepo;
    @Autowired
    private UserJPARepository userRepo;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running CommandLine Startup");
        this.createInitialProducts();
        this.createInitialUsers();
    }

    private void createInitialProducts() {
        Product product = new Product("Solar panel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen.");
        this.productRepo.save(product);
    }

    private void createInitialUsers() {
        UserModel admin1 = new UserModel(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), PermissionLevel.ADMIN, "Kevin Bakker", "kevin@solar.nl", LocalDate.now(), "admin");
        this.userRepo.save(admin1);

        UserModel viewer1 = new UserModel(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), PermissionLevel.VIEWER, "Léon Dolmans", "leonr@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer1);

        UserModel viewer2 = new UserModel(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"), PermissionLevel.VIEWER, "Mark Dijkstra", "mark@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer2);

        UserModel viewer3 = new UserModel(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"), PermissionLevel.VIEWER, "Tobi Kok", "tobi@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer3);

        UserModel viewer4 = new UserModel(UUID.fromString("550e8400-e29b-41d4-a716-446655440004"), PermissionLevel.VIEWER, "Ryan-Jay Takkenberg", "mark@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer4);

        UserModel viewer5 = new UserModel(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"), PermissionLevel.VIEWER, "Hamza Zaraoui", "mark@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer5);

    }

}
