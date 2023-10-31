package app;

import app.models.Product;
import app.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsserverApplication {

    public static final String CROSS_ORIGIN = "http://localhost:8080";

    public static void main(String[] args) {
        SpringApplication.run(EsserverApplication.class, args);

        User.list.add(new User("550e8400-e29b-41d4-a716-446655440000"));

        Product.list.add(new Product(0, "Zonnepaneel", 150.123, "Heeft een vermogen van 430 Wattpiek en beschikt over 108 cellen."));
    }

}
