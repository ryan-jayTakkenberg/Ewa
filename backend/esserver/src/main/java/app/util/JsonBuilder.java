package app.util;

import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.models.Order;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author leon
 */
public class JsonBuilder {

    private final JsonNode json;

    public static JsonBuilder parse(JsonNode json) {
        if (json == null) {
            throw new BadRequestException("No JSON body supplied");
        }
        return new JsonBuilder(json);
    }

    private JsonBuilder(JsonNode json) {
        this.json = json;
    }

    public List<JsonBuilder> getArrayFromField(String field) {
        if (!json.has(field)) {
            String errorMessage = String.format("Missing field: '%s'", field);
            System.err.println(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        if (!json.get(field).isArray()) {
            String errorMessage = String.format("Field '%s' must be an array", field);
            System.err.println(errorMessage);
            throw new BadRequestException(errorMessage);
        }

        return StreamSupport.stream(json.get(field).spliterator(), false)// Convert each element to its JSON string representation
                .map(JsonBuilder::parse)  // Parse each JSON string into a JsonBuilder
                .collect(Collectors.toList());
    }

    public int getIntFromField(String field) {
        if (!json.has(field)) {
            throw new BadRequestException(String.format("Missing field: '%s'", field));
        }
        if (!json.get(field).isNumber()) {
            throw new BadRequestException(String.format("Field '%s' must be a number", field));
        }
        return json.get(field).asInt();
    }

    public long getLongFromField(String field) {
        if (!json.has(field)) {
            String errorMessage = String.format("Missing field: '%s'", field);
            System.err.println(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        if (!json.get(field).isNumber()) {
            String errorMessage = String.format("Field '%s' must be a number", field);
            System.err.println(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        return json.get(field).asLong();
    }

    public String getStringFromField(String field) {
        if (!json.has(field)) {
            throw new BadRequestException(String.format("Missing field: '%s'", field));
        }
        if (!json.get(field).isTextual()) {
            throw new BadRequestException(String.format("Field '%s' must be a string", field));
        }
        return json.get(field).asText();
    }

    public LocalDate getDateFromField(String field) {
        String stringValue = getStringFromField(field);
        if (stringValue == null) {
            return null;
        }

        try {
            // Parse the string to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(stringValue, formatter);
        } catch (Exception e) {
            // Handle parsing errors, e.g., if the string is not a valid date
            throw new BadRequestException(String.format("Error parsing date for field '%s'", field));
        }
    }

    public <E extends Enum<E>> E getEnumFromField(String field, Class<E> enumClass) {
        String stringValue = getStringFromField(field);
        if (stringValue == null) {
            return null;
        }

        E[] enumConstants = enumClass.getEnumConstants();

        if (enumConstants != null) {
            for (E enumValue : enumConstants) {
                if (enumValue.name().equals(field) && stringValue.equalsIgnoreCase(enumValue.toString())) {
                    return enumValue;
                }
            }
        }


        throw new BadRequestException(String.format("Field '%s' must be a valid enum value", field));
    }

    public boolean getBooleanFromField(String field) {
        if (!json.has(field)) {
            throw new BadRequestException(String.format("Missing field: '%s'", field));
        }
        if (!json.get(field).isBoolean()) {
            throw new BadRequestException(String.format("Field '%s' must be a boolean", field));
        }
        return json.get(field).asBoolean();
    }

    public double getDoubleFromField(String field) {
        if (!json.has(field)) {
            throw new BadRequestException(String.format("Missing field: '%s'", field));
        }
        if (!json.get(field).isNumber()) {
            throw new BadRequestException(String.format("Field '%s' must be a number", field));
        }
        return json.get(field).asDouble();
    }

    @Override
    public String toString() {
        return json.toString();
    }

}
