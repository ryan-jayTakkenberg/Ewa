package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsserverApplication {

    /*
     * BACKEND:
     * If you need to create something, and you don't know where to start, look at ProductController, it has endpoints with the most CRUD actions
     * Also take a look at the Product model
     */

    public static void main(String[] args) {
        SpringApplication.run(EsserverApplication.class, args);
    }

}
