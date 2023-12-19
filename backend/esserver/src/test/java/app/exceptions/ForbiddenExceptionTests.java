package app.exceptions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ForbiddenExceptionTests {

    private final int FORBIDDEN_STATUS_CODE = HttpStatus.FORBIDDEN.value();

    private static String forbiddenMessage;
    private static ForbiddenException forbiddenException;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        forbiddenMessage = "forbidden exception message";
        forbiddenException = new ForbiddenException(forbiddenMessage);

        assertNotNull(forbiddenMessage);
        assertNotNull(forbiddenException);
    }

    @Test
    void testStatusCode() {
        int statusCode = forbiddenException.getStatusCode().value();
        assertEquals(statusCode, FORBIDDEN_STATUS_CODE);
    }

    @Test
    void testMessage() {
        String message = forbiddenException.getReason();
        assertEquals(message, forbiddenMessage);
    }

}
