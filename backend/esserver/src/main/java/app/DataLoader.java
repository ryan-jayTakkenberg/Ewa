package app;

import app.enums.PermissionLevel;
import app.models.*;
import app.repositories.*;
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
        this.createInitialUsers();
        this.createSampleReports();
        this.createSampleOrders();
        this.createSampleTeamsAndProjects();
        this.createSampleWarehouses();

        System.out.println("Done!");
    }

    private void createInitialProducts() {
        this.productRepo.save(new Product("Solar panel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen."));
        this.productRepo.save(new Product("Motor", 32.54, "Heeft een vermogen van 1000 Watt, 72V"));
        this.productRepo.save(new Product("Frame", 5423.23, "Sterk frame van goede metalen"));
    }

    private void createSampleReports() {
        this.reportRepo.save(new Report(1, "Hello Jason, please notice that at the end of this week project 5 is due.", "19/11/2023", 1, "admin", 2));
        this.reportRepo.save(new Report(2, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "20/11/2023", 2, "Viewer", 1));
        this.reportRepo.save(new Report(3,"Travelling alteration impression six all uncommonly. Chamber hearing inhabit joy highest private ask him our believe. Up nature valley do warmly. Entered of cordial do on no hearted.", "21/11/2023", 1, "admin", 2));
        this.reportRepo.save(new Report(4, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "21/11/2023", 1, "admin", 2));
    }

    private void createSampleOrders() {
        this.orderRepo.save(new Order(-1, "4Blue", LocalDate.parse("2023-09-11"), LocalDate.parse("2023-11-19"), 1, 1, 100, Order.OrderStatus.CANCELED));
        this.orderRepo.save(new Order(-1, "Stralend groen", LocalDate.parse("2023-09-11"), LocalDate.parse("2023-11-19"), 2, 5, 20, Order.OrderStatus.DELIVERED));
        this.orderRepo.save(new Order(-1, "ZiezoSolar", LocalDate.parse("2023-09-11"), LocalDate.parse("2023-11-19"), 3, 2, 41, Order.OrderStatus.PENDING));
    }

    private void createSampleTeamsAndProjects(){

        Teams team1 = this.teamsRepo.save(new Teams(PermissionLevel.ADMIN,0,"Team Bijlmer", "Warehouse OOST"));
        Teams team2 = this.teamsRepo.save(new Teams(PermissionLevel.ADMIN,0,"Team Aalsmeer", "Warehouse AALSMEER"));
        Teams team3 = this.teamsRepo.save(new Teams(PermissionLevel.ADMIN,0,"Team Purmerend", "Warehouse PURMEREND"));
        this.teamsRepo.save(new Teams(PermissionLevel.VIEWER,0,"Team Zaandam", "Warehouse ZAANDAM"));
        this.teamsRepo.save(new Teams(PermissionLevel.VIEWER,0,"Team West", "Warehouse WEST"));

        LocalDate installDate = LocalDate.of(2023, 11, 21);
        this.projectsRepo.save(new Project(1, "Blue", "HVA", installDate, "Project to install solar panels to Company A", team1));
        this.projectsRepo.save(new Project(2, "Red", "Company B", installDate, "Project to install solar panels to Company B", team1));
        this.projectsRepo.save(new Project(3, "Green", "HVA", installDate, "Project to install solar panels to Company C", team2));
        this.projectsRepo.save(new Project(4, "Yellow", "Company A", installDate, "Project to install solar panels to Company D", team3));
    }

    private void createInitialUsers() {
        this.userRepo.save(new User(PermissionLevel.ADMIN, "admin", "admin@solar.nl", LocalDate.now(), "admin"));
        this.userRepo.save(new User(PermissionLevel.VIEWER, "viewer", "viewer@solar.nl", LocalDate.now(), "viewer"));

        User viewer1 = new User(PermissionLevel.VIEWER, "H1", "test1@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer1);

        User viewer2 = new User(PermissionLevel.VIEWER, "H2", "test2@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer2);

        User viewer3 = new User(PermissionLevel.VIEWER, "H3", "test3@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer3);

        User viewer4 = new User(PermissionLevel.VIEWER, "H4", "test4@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer4);

        User viewer5 = new User(PermissionLevel.VIEWER, "H5", "test5@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer5);

    }

    private void createSampleWarehouses(){
        this.warehouseRepo.save(new Warehouse(1, "Solar Sedum", "Amsterdam", "Straat 111", "1234 AB"));
        this.warehouseRepo.save(new Warehouse(2, "HvA Warehose", "Amsterdam", "Straat 222", "1234 CD"));
        this.warehouseRepo.save(new Warehouse(3, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF"));
        this.warehouseRepo.save(new Warehouse(4, "Green Left", "Amsterdam", "Straat 444", "1234 GH"));
    }

}
