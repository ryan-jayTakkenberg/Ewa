package app;

import app.models.Product;
import app.repositories.EntityRepositoryJPA;
import app.repositories.ProductJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EntityRepositoryJPA<Product> productsRepo;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running CommandLine Startup");
        this.createInitialProducts();
    }

    private void createInitialProducts() {
        Product product = new Product(0, "Solar panel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen.");
        this.productsRepo.save(product);
    }

}
