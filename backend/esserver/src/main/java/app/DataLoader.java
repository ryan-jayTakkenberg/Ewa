package app;

import app.enums.PermissionLevel;
import app.models.*;
import app.models.relations.Product_Order;
import app.models.relations.Product_Warehouse;
import app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductJPARepository productsRepo;
    @Autowired
    private UserJPARepository userRepo;
    @Autowired
    private ReportJPARepository reportRepo;
    @Autowired
    private OrderJPARepository orderRepo;
    @Autowired
    private ProjectJPARepository projectsRepo;
    @Autowired
    private TeamJPARepository teamsRepo;
    @Autowired
    private WarehouseJPARepository warehouseRepo;

    @Override
    public void run(String... args) {
        System.out.println("Running CommandLine Startup...");

        this.createInitialProducts();
        this.createSampleWarehouses();
        this.createSampleReports();
        this.createSampleOrders();
        this.createSampleTeamAndProjects();
        this.createInitialUsers();



        System.out.println("Done!");
    }

    private void createInitialProducts() {
        this.productsRepo.save(new Product("Solar panel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen."));
        this.productsRepo.save(new Product("Motor", 32.54, "Heeft een vermogen van 1000 Watt, 72V"));
        this.productsRepo.save(new Product("Frame", 5423.23, "Sterk frame van goede metalen"));
        this.productsRepo.save(new Product("Solar Panel GTX", 219.99, "New panel GTX technology"));
        this.productsRepo.save(new Product("Solar Battery", 29.99, "80.000 Mah battery"));
    }

    private void createSampleReports() {
        this.reportRepo.save(new Report(1, "Hello Jason, please notice that at the end of this week project 5 is due.", "19/11/2023", 1, "admin", 2));
        this.reportRepo.save(new Report(2, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "20/11/2023", 2, "Viewer", 1));
        this.reportRepo.save(new Report(3, "Travelling alteration impression six all uncommonly. Chamber hearing inhabit joy highest private ask him our believe. Up nature valley do warmly. Entered of cordial do on no hearted.", "21/11/2023", 1, "admin", 2));
        this.reportRepo.save(new Report(4, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "21/11/2023", 1, "admin", 2));
    }


    private void createSampleWarehouses() {
        Warehouse[] warehouses = {
                new Warehouse(0, "Solar Sedum", "Amsterdam", "Straat 111", "1234 AB"),
                new Warehouse(0, "HvA Warehouse", "Amsterdam", "Straat 222", "1234 CD"),
                new Warehouse(0, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF"),
                new Warehouse(0, "Green Left", "Amsterdam", "Straat 444", "1234 GH")
        };

        List<Product> products = productsRepo.findAll();
        Random random = new Random();

        for (Warehouse warehouse : warehouses) {
            Product_Warehouse product_warehouse = new Product_Warehouse(random.nextInt(10) + 1, products.stream().skip(random.nextInt(products.size())).findFirst().orElse(null), warehouse);
            warehouse.addProduct(product_warehouse);
            warehouseRepo.save(warehouse);
        }
    }


    private void createSampleTeamAndProjects() {
        Team team1 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN, 0, "Team Bijlmer", warehouseRepo.findById(1)));
        Team team2 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN, 0, "Team Aalsmeer", warehouseRepo.findById(2)));
        Team team3 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN, 0, "Team Purmerend", warehouseRepo.findById(3)));

        LocalDate installDate = LocalDate.of(2023, 11, 21);
        this.projectsRepo.save(new Project(1, "Blue", "HVA", installDate, "Project to install solar panels to Company A", team1));
        this.projectsRepo.save(new Project(2, "Red", "Company B", installDate, "Project to install solar panels to Company B", team1));
        this.projectsRepo.save(new Project(3, "Green", "HVA", installDate, "Project to install solar panels to Company C", team2));
        this.projectsRepo.save(new Project(4, "Yellow", "Company A", installDate, "Project to install solar panels to Company D", team3));
    }

    private void createInitialUsers() {
        Team team4 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN, 0, "Team Noord-Holland", warehouseRepo.findById(1)));
        Team team2 = this.teamsRepo.save(new Team(PermissionLevel.VIEWER, 0, "Team Hallo", warehouseRepo.findById(2)));
        Team team3 = this.teamsRepo.save(new Team(PermissionLevel.VIEWER, 0, "Team Test", warehouseRepo.findById(3)));

        this.userRepo.save(new User(PermissionLevel.ADMIN, "admin", "admin@solar.nl", LocalDate.now(), "admin", null));

        User userTeam1 = new User(PermissionLevel.VIEWER, "viewer", "viewer@solar.nl", LocalDate.now(), "viewer", team4);
        this.userRepo.save(userTeam1);
        userTeam1.setTeam(team4);

        User viewer1 = new User(PermissionLevel.VIEWER, "H1", "test1@solar.com", LocalDate.now(), "viewer", team2);
        this.userRepo.save(viewer1);
        userTeam1.setTeam(team2);

        User viewer2 = new User(PermissionLevel.VIEWER, "H2", "test2@solar.com", LocalDate.now(), "viewer", null);
        this.userRepo.save(viewer2);

        User viewer3 = new User(PermissionLevel.VIEWER, "H3", "test3@solar.com", LocalDate.now(), "viewer", team3);
        this.userRepo.save(viewer3);
        userTeam1.setTeam(team3);

        User viewer4 = new User(PermissionLevel.VIEWER, "H4", "test4@solar.com", LocalDate.now(), "viewer", null);
        this.userRepo.save(viewer4);

        User viewer5 = new User(PermissionLevel.VIEWER, "H5", "test5@solar.com", LocalDate.now(), "viewer", null);
        this.userRepo.save(viewer5);
    }

    private void createSampleOrders() {
        // Create Warehouse
        Warehouse warehouse1 = new Warehouse(1, "Warehouse solar", "Amsterdam", "Hoge Solarstraat 3", "5G5GHA");
        warehouseRepo.save(warehouse1);
        Warehouse warehouse2 = new Warehouse(1, "Warehouse neeman", "Amsterdam", "Hoge Solarstraat 3", "5G5GHA");
        warehouseRepo.save(warehouse1);

        // Create Teams
        Team team1 = new Team(PermissionLevel.ADMIN, 1, "Team 1", warehouse1 );
        teamsRepo.save(team1);
        Team team2 = new Team(PermissionLevel.ADMIN, 2, "Team 2", warehouse2 );
        teamsRepo.save(team2);

        // Create Order and associate with Team and Product
        Order order1 = new Order();
        order1.setName("Restock Solar Panels");
        order1.setOrderedFrom("Solar City");
        order1.setOrderDate(LocalDate.now());
        order1.setEstimatedDeliveryDate(LocalDate.now().plusDays(7));
        order1.setTeam(team1);
        order1.setStatus(Order.OrderStatus.PENDING);

        // Associate order with products and add the Product quantity
        order1.addProduct(new Product_Order(2, productsRepo.findById(1), order1));
        order1.addProduct(new Product_Order(1, productsRepo.findById(2), order1));

        orderRepo.save(order1);

        // Create Order 2 and associate with Team and Product
        Order order2 = new Order();
        order2.setName("Frames and new solar panels");
        order2.setOrderedFrom("Green Supply");
        order2.setOrderDate(LocalDate.now());
        order2.setEstimatedDeliveryDate(LocalDate.now().plusDays(34));
        order2.setTeam(team2);
        order2.setStatus(Order.OrderStatus.PENDING);

        // Associate order with products and add the Product quantity
        order2.addProduct(new Product_Order(2, productsRepo.findById(3), order2));
        order2.addProduct(new Product_Order(8, productsRepo.findById(4), order2));

        orderRepo.save(order2);
    }
}
