package app.util;

import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.server.ResponseStatusException;

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

}
