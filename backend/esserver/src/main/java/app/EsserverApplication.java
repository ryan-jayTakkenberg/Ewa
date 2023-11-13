package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsserverApplication {

    public static final String CROSS_ORIGIN = "http://localhost:8080";

    public static void main(String[] args) {
        SpringApplication.run(EsserverApplication.class, args);
    }

}
