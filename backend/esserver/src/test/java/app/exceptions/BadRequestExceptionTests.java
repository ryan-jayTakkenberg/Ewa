package app.exceptions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BadRequestExceptionTests {

    private final int BAD_REQUEST_STATUS_CODE = HttpStatus.BAD_REQUEST.value();

    private static String badRequestMessage;
    private static BadRequestException badRequestException;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        badRequestMessage = "bad request exception message";
        badRequestException = new BadRequestException(badRequestMessage);

        assertNotNull(badRequestMessage);
        assertNotNull(badRequestException);
    }

    @Test
    void testStatusCode() {
        int statusCode = badRequestException.getStatusCode().value();
        assertEquals(statusCode, BAD_REQUEST_STATUS_CODE);
    }

    @Test
    void testMessage() {
        String message = badRequestException.getReason();
        assertEquals(message, badRequestMessage);
    }

}
