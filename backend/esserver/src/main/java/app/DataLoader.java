package app;

import app.enums.PermissionLevel;
import app.models.*;
import app.repositories.ProductJPARepository;
import app.repositories.UserJPARepository;
import app.repositories.ReportJPARepository;
import app.repositories.OrderJPARepository;
import app.repositories.ProjectJPARepository;
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

    @Override
    public void run(String... args) {
        System.out.println("Running CommandLine Startup...");

        this.createInitialProducts();
        this.createInitialUsers();
        this.createSampleReports();
        this.createSampleOrders();
        this.createSampleProjects();

        System.out.println("Done!");
    }

    private void createInitialProducts() {
        this.productRepo.save(new Product("Solar panel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen."));
        this.productRepo.save(new Product("Motor", 32.54, "Heeft een vermogen van 1000 Watt, 72V"));
        this.productRepo.save(new Product("Frame", 5423.23, "Sterk frame van goede metalen"));
    }

    private void createSampleReports() {
        this.reportRepo.save(new Report(1, "Hello Jason, please notice that at the end of this week project 5 is due.", "19/11/2023", "admin", "viewer"));
        this.reportRepo.save(new Report(2, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "20/11/2023", "viewer", "admin"));
        this.reportRepo.save(new Report(3, "Travelling alteration impression six all uncommonly. Chamber hearing inhabit joy highest private ask him our believe. Up nature valley do warmly. Entered of cordial do on no hearted.", "21/11/2023", "admin", "viewer"));
        this.reportRepo.save(new Report(4, "Be me shall purse my ought times. Joy years doors all would again rooms these. Solicitude announcing as to sufficient my. No my reached suppose proceed pressed perhaps he. Eagerness it delighted pronounce repulsive furniture no.", "21/11/2023", "admin", "viewer"));
    }


    private void createSampleOrders() {

        this.orderRepo.save(new Order(1, "We need these products ASAP", "19/11/2023", "Solar Panel", 12));
        this.orderRepo.save(new Order(2, "Hi Admin, please order these products", "19/11/2023", "Motor", 8));
        this.orderRepo.save(new Order(3, "We've run out of solar panels", "19/11/2023", "Frame", 20));
    }

    private void createSampleProjects() {

        LocalDate installDate = LocalDate.of(2023, 11, 21);
        this.projectsRepo.save(new Project(1, "Blue", "HVA", installDate, "10S", "23E", "Project to install solar panels to Company A"));
        this.projectsRepo.save(new Project(2, "Red", "Company B", installDate, "3S", "2", "Project to install solar panels to Company B"));
        this.projectsRepo.save(new Project(3, "Green", "HVA", installDate, "5D", "26E", "Project to install solar panels to Company C"));
        this.projectsRepo.save(new Project(4, "Yellow", "Company A", installDate, "8T", "15A", "Project to install solar panels to Company D"));
    }

    private void createInitialUsers() {
        this.userRepo.save(new UserModel(PermissionLevel.ADMIN, "admin", "admin@solar.nl", LocalDate.now(), "admin"));
        this.userRepo.save(new UserModel(PermissionLevel.VIEWER, "viewer", "viewer@solar.nl", LocalDate.now(), "viewer"));

        UserModel viewer1 = new UserModel(PermissionLevel.VIEWER, "H1", "test1@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer1);

        UserModel viewer2 = new UserModel(PermissionLevel.VIEWER, "H2", "test2@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer2);

        UserModel viewer3 = new UserModel(PermissionLevel.VIEWER, "H3", "test3@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer3);

        UserModel viewer4 = new UserModel(PermissionLevel.VIEWER, "H4", "test4@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer4);

        UserModel viewer5 = new UserModel(PermissionLevel.VIEWER, "H5", "test5@solar.com", LocalDate.now(), "viewer");
        this.userRepo.save(viewer5);

    }

}
