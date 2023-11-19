package app;

import app.enums.PermissionLevel;
import app.models.Product;
import app.models.UserModel;
import app.models.Report;
import app.models.Order;
import app.repositories.ProductJPARepository;
import app.repositories.UserJPARepository;
import app.repositories.ReportJPARepository;
import app.repositories.OrderJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductJPARepository productRepo;
    @Autowired
    private UserJPARepository userRepo;
    @Autowired
    private ReportJPARepository reportRepo;
    @Autowired
    private OrderJPARepository orderRepo;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running CommandLine Startup");
        this.createInitialProducts();
        this.createInitialUsers();
        this.createSampleReports();
        this.createSampleOrders();
    }

    private void createInitialProducts() {
        this.productRepo.save(new Product("Solar panel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen."));
        this.productRepo.save(new Product("Motor", 32.54, "Heeft een vermogen van 1000 Watt, 72V"));
        this.productRepo.save(new Product("Frame", 5423.23, "Sterk frame van goede metalen"));
    }

    private void createSampleReports() {
        this.reportRepo.save(new Report(1, "lorem ipsum", "19/11/2023", "admin", "viewer"));
        this.reportRepo.save(new Report(2, "lorem ipsum", "19/11/2023", "viewer", "admin"));
        this.reportRepo.save(new Report(3, "lorem ipsum", "19/11/2023", "admin", "viewer"));
    }

    private void createSampleOrders() {

        this.orderRepo.save(new Order(1, "We need these products ASAP", "19/11/2023", "Solar Panel", 12));
        this.orderRepo.save(new Order(2, "Hi Admin, please order these products", "19/11/2023", "Motor", 8));
        this.orderRepo.save(new Order(3, "We've run out of solar panels", "19/11/2023", "Frame", 20));
    }

    private void createInitialUsers() {
        this.userRepo.save(new UserModel(PermissionLevel.ADMIN, "admin", "admin@solar.nl", LocalDate.now(), "admin"));
        this.userRepo.save(new UserModel(PermissionLevel.VIEWER, "viewer", "viewer@solar.nl", LocalDate.now(), "viewer"));

        UserModel admin1 = new UserModel(PermissionLevel.ADMIN, "Kevin Bakker", "kevin@solar.nl", LocalDate.now(), "admin");
        this.userRepo.save(admin1);

        UserModel viewer1 = new UserModel(PermissionLevel.VIEWER, "Léon Dolmans", "leonr@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer1);

        UserModel viewer2 = new UserModel(PermissionLevel.VIEWER, "Mark Dijkstra", "mark@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer2);

        UserModel viewer3 = new UserModel(PermissionLevel.VIEWER, "Tobi Kok", "tobi@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer3);

        UserModel viewer4 = new UserModel(PermissionLevel.VIEWER, "Ryan-Jay Takkenberg", "mark@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer4);

        UserModel viewer5 = new UserModel(PermissionLevel.VIEWER, "H1", "test@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer5);

        UserModel viewer6 = new UserModel(PermissionLevel.VIEWER, "H2", "test@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer6);

        UserModel viewer7 = new UserModel(PermissionLevel.VIEWER, "H3", "test@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer7);

        UserModel viewer8 = new UserModel(PermissionLevel.VIEWER, "H4", "test@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer8);

        UserModel viewer9 = new UserModel(PermissionLevel.VIEWER, "H5", "test@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer9);

    }

}
