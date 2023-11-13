package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsserverApplication {

    /*
     * BACKEND:
     * Check out basic info about routes at 'app/routes/ProductRoutes'
     * Check out basic info about models at 'app/models/Product'
     */

    public static void main(String[] args) {
        SpringApplication.run(EsserverApplication.class, args);
    }

}
