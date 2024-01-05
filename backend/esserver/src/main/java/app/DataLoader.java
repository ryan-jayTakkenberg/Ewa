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
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductJPARepository productsRepo;
    @Autowired Product_OrderJPARepository product_OrderRepo;
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
                new Warehouse(0, "Solar Sedum", "Amsterdam", "Straat 111", "1234 AB", 100, 20, 0),
                new Warehouse(0, "HvA Warehouse", "Amsterdam", "Straat 222", "1234 CD", 100, 20, 0),
                new Warehouse(0, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF", 100, 20, 0),
                new Warehouse(0, "Green Left", "Amsterdam", "Straat 444", "1234 GH", 100, 20, 0)
        };

        List<Product> products = productsRepo.findAll();
        Random random = new Random();

        for (Warehouse warehouse : warehouses) {
            Product_Warehouse product_warehouse = new Product_Warehouse(random.nextInt(100) + 1, products.stream().skip(random.nextInt(products.size())).findFirst().orElse(null), warehouse);
            warehouse.addProduct(product_warehouse);
            warehouseRepo.save(warehouse);
        }
    }


    private void createSampleTeamAndProjects() {
        Team team1 = this.teamsRepo.save(new Team("Team Bijlmer", warehouseRepo.findById(1)));
        Team team2 = this.teamsRepo.save(new Team( "Team Aalsmeer", warehouseRepo.findById(2)));
        Team team3 = this.teamsRepo.save(new Team("Team Purmerend", warehouseRepo.findById(3)));

        LocalDate installDate = LocalDate.of(2023, 11, 21);
        this.projectsRepo.save(new Project(1, "Blue", "HVA", installDate, "Project to install solar panels to Company A", team1));
        this.projectsRepo.save(new Project(2, "Red", "Company B", installDate, "Project to install solar panels to Company B", team1));
        this.projectsRepo.save(new Project(3, "Green", "HVA", installDate, "Project to install solar panels to Company C", team2));
        this.projectsRepo.save(new Project(4, "Yellow", "Company A", installDate, "Project to install solar panels to Company D", team3));
    }

    private void createInitialUsers() {
        // Create Admin account
        User userAdmin = new User(PermissionLevel.ADMIN, "admin", "admin@solar.nl", LocalDate.now(), "admin", null);
        this.userRepo.save(userAdmin);

        // Create User account
        User userViewer = new User(PermissionLevel.VIEWER, "viewer", "viewer@solar.nl", LocalDate.now(), "viewer", teamsRepo.findById(1));
        this.userRepo.save(userViewer);
    }

    private void createSampleOrders() {
        Warehouse warehouse1 = new Warehouse(1, "Warehouse solar", "Amsterdam", "Hoge Solarstraat 3", "5G5GHA", 100, 20, 0);
        warehouseRepo.save(warehouse1);

        Team team1 = new Team(  "Team 1", warehouse1);
        teamsRepo.save(team1);

        // Create Order and associate with Team
        Order order1 = new Order();
        order1.setName("Test Order");
        order1.setOrderedFrom("Solar City");
        order1.setOrderDate(LocalDate.now());
        order1.setEstimatedDeliveryDate(LocalDate.now().plusDays(7));
        order1.setTeam(team1);
        order1.setStatus(Order.OrderStatus.PENDING);

        // Add sample products to the order (assuming productRepo is a repository for Product)
        Product product1 = new Product("Product 1", 10.0, "Description 1");
        productsRepo.save(product1);

        Product product2 = new Product("Product 2", 20.0, "Description 2");
        productsRepo.save(product2);

        // Create and associate Product_Order entities
        Product_Order productOrder1 = new Product_Order(2, product1, order1);
        Product_Order productOrder2 = new Product_Order(3, product2, order1);

        // Associate ordered products with the order
        order1.addOrderedProduct(productOrder1);
        order1.addOrderedProduct(productOrder2);

        // Save the order along with associated product orders
        orderRepo.save(order1);
    }
}
