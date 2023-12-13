package app;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EsserverApplicationTests {

    @Autowired
    EsserverApplication application = null;

    @Test
    void contextLoads() {
        assertNotNull(application);
    }

}
