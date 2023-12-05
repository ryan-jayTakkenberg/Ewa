package app;

import app.enums.PermissionLevel;
import app.models.*;
import app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    private ProjectJPARepository projectsRepo;
    @Autowired
    private TeamJPARepository teamsRepo;

    @Autowired
    private WarehouseJPARepository warehouseRepo;

   private Warehouse warehouse1;
    private Warehouse warehouse2;
    private Warehouse warehouse3;




    @Override
    public void run(String... args) {
        System.out.println("Running CommandLine Startup...");

        this.createInitialProducts();
        this.createSampleReports();
        this.createSampleWarehouses();
        this.createSampleTeamAndProjects();
        this.createInitialUsers();
        this.createSampleOrders();

        System.out.println("Done!");
    }

    private void createInitialProducts() {
        this.productRepo.save(new ProductInfo("Solar panel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen."));
        this.productRepo.save(new ProductInfo("Motor", 32.54, "Heeft een vermogen van 1000 Watt, 72V"));
        this.productRepo.save(new ProductInfo("Frame", 5423.23, "Sterk frame van goede metalen"));
    }

    private void createSampleReports() {
        this.reportRepo.save(new Report(1, "Hello Jason, please notice that at the end of this week project 5 is due.", "19/11/2023", 1, "admin", 2));
        this.reportRepo.save(new Report(2, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "20/11/2023", 2, "Viewer", 1));
        this.reportRepo.save(new Report(3,"Travelling alteration impression six all uncommonly. Chamber hearing inhabit joy highest private ask him our believe. Up nature valley do warmly. Entered of cordial do on no hearted.", "21/11/2023", 1, "admin", 2));
        this.reportRepo.save(new Report(4, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "21/11/2023", 1, "admin", 2));
    }





    private void createSampleOrders() {
        // Retrieve products from the database
        List<ProductInfo> productInfos = productRepo.findAll();
        List<Team> teams = teamsRepo.findAll();

        // Create sample orders with associated products
        Order order1 = new Order(-1, "4Blue", LocalDate.parse("2023-09-11"), LocalDate.parse("2023-11-19"), teams.get(1), productInfos.subList(0, 2), Order.OrderStatus.CANCELED);
        Order order2 = new Order(-1, "Stralend groen", LocalDate.parse("2023-09-11"), LocalDate.parse("2023-11-19"), teams.get(1), productInfos.subList(1, 3), Order.OrderStatus.DELIVERED);
        Order order3 = new Order(-1, "ZiezoSolar", LocalDate.parse("2023-09-11"), LocalDate.parse("2023-11-19"), teams.get(3), productInfos.subList(2, 3), Order.OrderStatus.PENDING);

        // Save orders to the database
        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);
    }


    private void createSampleWarehouses(){
        warehouse1 = this.warehouseRepo.save(new Warehouse(1, "Solar Sedum", "Amsterdam", "Straat 111", "1234 AB"));
        warehouse2 = this.warehouseRepo.save(new Warehouse(2, "HvA Warehose", "Amsterdam", "Straat 222", "1234 CD"));
        warehouse3 = this.warehouseRepo.save(new Warehouse(3, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF"));
        this.warehouseRepo.save(new Warehouse(4, "Green Left", "Amsterdam", "Straat 444", "1234 GH"));
    }


    private void createSampleTeamAndProjects(){
        Team team1 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN, 0, "Team Bijlmer", warehouse1));
         Team team2 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN,0,"Team Aalsmeer", warehouse2));
         Team team3 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN,0,"Team Purmerend", warehouse3));

        LocalDate installDate = LocalDate.of(2023, 11, 21);
        this.projectsRepo.save(new Project(1, "Blue", "HVA", installDate, "Project to install solar panels to Company A", team1));
        this.projectsRepo.save(new Project(2, "Red", "Company B", installDate, "Project to install solar panels to Company B", team1));
        this.projectsRepo.save(new Project(3, "Green", "HVA", installDate, "Project to install solar panels to Company C", team2));
        this.projectsRepo.save(new Project(4, "Yellow", "Company A", installDate, "Project to install solar panels to Company D", team3));
    }
    private void createInitialUsers() {
        Team team4 = this.teamsRepo.save(new Team(PermissionLevel.ADMIN, 0, "Team Noord-Holland", warehouse1));
        Team team2 = this.teamsRepo.save(new Team(PermissionLevel.VIEWER, 0, "Team Hallo", warehouse2));
        Team team3 = this.teamsRepo.save(new Team(PermissionLevel.VIEWER, 0, "Team Test", warehouse3));

        this.userRepo.save(new User(PermissionLevel.ADMIN, "admin", "admin@solar.nl", LocalDate.now(), "admin", null));

        User userTeam1 = new User(PermissionLevel.VIEWER, "viewer", "viewer@solar.nl", LocalDate.now(), "viewer", team4);
        this.userRepo.save(userTeam1);
        team4.setUser(userTeam1);

        User viewer1 = new User(PermissionLevel.VIEWER, "H1", "test1@solar.com", LocalDate.now(), "viewer", team2);
        this.userRepo.save(viewer1);
        team2.setUser(viewer1);

        User viewer2 = new User(PermissionLevel.VIEWER, "H2", "test2@solar.com", LocalDate.now(), "viewer", null);
        this.userRepo.save(viewer2);

        User viewer3 = new User(PermissionLevel.VIEWER, "H3", "test3@solar.com", LocalDate.now(), "viewer", team3);
        this.userRepo.save(viewer3);
        team3.setUser(viewer3);

        User viewer4 = new User(PermissionLevel.VIEWER, "H4", "test4@solar.com", LocalDate.now(), "viewer", null);
        this.userRepo.save(viewer4);

        User viewer5 = new User(PermissionLevel.VIEWER, "H5", "test5@solar.com", LocalDate.now(), "viewer", null);
        this.userRepo.save(viewer5);
    }



}
