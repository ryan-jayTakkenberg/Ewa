package app.exceptions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class NotFoundExceptionTests {

    private final int NOT_FOUND_STATUS_CODE = HttpStatus.NOT_FOUND.value();

    private static String notFoundMessage;
    private static NotFoundException notFoundException;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        notFoundMessage = "not found exception message";
        notFoundException = new NotFoundException(notFoundMessage);

        assertNotNull(notFoundMessage);
        assertNotNull(notFoundException);
    }

    @Test
    void testStatusCode() {
        int statusCode = notFoundException.getStatusCode().value();
        assertEquals(statusCode, NOT_FOUND_STATUS_CODE);
    }

    @Test
    void testMessage() {
        String message = notFoundException.getReason();
        assertEquals(message, notFoundMessage);
    }

}
