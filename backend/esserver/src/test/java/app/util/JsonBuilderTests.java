package app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class JsonBuilderTests {

    static String name;
    static JsonBuilder jsonBuilder;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        name = "Leon";

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode node = objectMapper.createObjectNode();

        // Add key-value pairs to the JSON object
        node.put("success", true);
        node.put("name", name);
        node.put("age", 10);
        node.put("city", "Amsterdam");
        node.put("inhabitants", 918117L);
        node.put("date", LocalDate.now().toString());
        node.put("random", Math.random());

        jsonBuilder = JsonBuilder.parse(node);

        assertNotNull(jsonBuilder);
    }

    @Test
    void testGetBooleanValue() {
        boolean value = false;
        String fieldName = "success";
        String reason = null;

        try {
            value = jsonBuilder.getBooleanFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertNull(reason);
        assertTrue(value);
    }

    @Test
    void testGetStringValue() {
        String value = null;
        String fieldName = "name";
        String reason = null;

        try {
            value = jsonBuilder.getStringFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertNull(reason);
        assertEquals(value, name);
    }

    @Test
    void testGetInt() {
        String fieldName = "age";
        String reason = null;

        try {
            jsonBuilder.getIntFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertNull(reason);
    }

    @Test
    void testGetLong() {
        String fieldName = "inhabitants";
        String reason = null;

        try {
            jsonBuilder.getLongFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertNull(reason);
    }

    @Test
    void testGetDouble() {
        String fieldName = "random";
        String reason = null;

        try {
            jsonBuilder.getDoubleFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertNull(reason);
    }

    @Test
    void testGetDateValue() {
        LocalDate date = null;
        String fieldName = "date";
        String reason = null;

        try {
            date = jsonBuilder.getDateFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertNull(reason);
        assertNotNull(date);

        LocalDate currentDate = LocalDate.now();
        assertEquals(date.getDayOfMonth(), currentDate.getDayOfMonth());
        assertEquals(date.getMonthValue(), currentDate.getMonthValue());
        assertEquals(date.getYear(), currentDate.getYear());
    }

    @Test
    void testMissingFieldError() {
        String value = null;
        String fieldName = "non_existing_field";
        String reason = null;

        try {
            value = jsonBuilder.getStringFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertEquals(reason, String.format("Missing field: '%s'", fieldName));
        assertNull(value);
    }

    @Test
    void testIncorrectDatatypeFieldError() {
        int value = Integer.MIN_VALUE;
        String fieldName = "name";
        String reason = null;

        try {
            value = jsonBuilder.getIntFromField(fieldName);
        } catch (ResponseStatusException e) {
            reason = e.getReason();
        }

        assertEquals(reason, String.format("Field '%s' must be a number", fieldName));
        assertEquals(value, Integer.MIN_VALUE);
    }

}
