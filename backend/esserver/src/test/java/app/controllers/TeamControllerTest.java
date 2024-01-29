package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.Team;
import app.models.Warehouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The class for testing the TeamController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc for simulating HTTP requests

    private static String token; // JWT token for authentication

    /**
     * Arrange: Set up the JWT token for authentication.
     */
    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        // Arrange: Configuration of JWT and creation of a token for testing purposes
        JWTConfig JwtConfig = new JWTConfig();
        JWToken jwt = new JWToken(-1, PermissionLevel.ADMIN, "127.0.0.1", JwtConfig.getExpiration());
        token = jwt.encode(JwtConfig.getIssuer(), JwtConfig.getPassphrase());

        // Assert: Check if the token is not null
        assertNotNull(token);

        // Assert: Check if the token has a length greater than 0
        assertTrue(token.length() > 0);
    }

    /**
     * Arrange: Verify successful autowiring of MockMvc.
     */
    @BeforeEach
    void autowiredSuccessfully() {
        // Assert: Check if MockMvc is not null
        assertNotNull(mockMvc);
    }

    /**
     * Test: Retrieve teams.
     *
     * @throws Exception the exception that may be thrown
     */
    /**
     * FAST Test: Retrieve teams.
     * Focused: This test focuses on retrieving teams.
     * Automated: It is automated to run with the test suite.
     * Small: It tests a specific feature - team retrieval.
     * Testable: It's designed to be easily testable and maintainable.
     */
    @Test
    void getTeams() throws Exception {
        // Arrange: Set up an HTTP GET request to "/teams" with the JWT token in the header
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/teams")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // Act: Perform the HTTP request
        ResultActions response = mockMvc.perform(builder);

        // Assert: Expect HTTP status OK and the result to be a JSON array
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    /**
     * Test: Add a new team.
     *
     * @throws Exception the exception that may be thrown
     */
    /**
     * FAST Test: Add a new team.
     * Focused: This test focuses on adding a new team.
     * Automated: It is automated to run with the test suite.
     * Small: It tests a specific feature - adding a new team.
     * Testable: It's designed to be easily testable and maintainable.
     */
    @Test
    void addNewTeam() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Arrange: Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name", "Unit test team");
        teamJson.put("warehouseId", 1902);

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        // Arrange: Set up an HTTP POST request to "/teams" with the JSON team object and the JWT token in the header
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // Act: Perform the HTTP request
        ResultActions response = mockMvc.perform(builder);

        // Assert: Expect HTTP status Created, an existing ID, and the team name to match
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Unit test team"));

    }

    /**
     * Test: Delete a team.
     *
     * @throws Exception the exception that may be thrown
     */
    /**
     * FAST Test: Delete a team.
     * Focused: This test focuses on deleting a team.
     * Automated: It is automated to run with the test suite.
     * Small: It tests a specific feature - deleting a team.
     * Testable: It's designed to be easily testable and maintainable.
     */
    @Test
    void deleteTeam() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Arrange: Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name","Team Green");
        teamJson.put("warehouseId",1902 );

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        // Arrange: Create a team to delete
        MockHttpServletRequestBuilder createBuilder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // Act: Perform the HTTP request to create the team
        ResultActions createResponse = mockMvc.perform(createBuilder);
        createResponse.andExpect(status().isCreated());

        // Arrange: Set up an HTTP DELETE request to "/teams/{id}" with the team ID as a path variable and the JWT token in the header
        MockHttpServletRequestBuilder deleteBuilder = MockMvcRequestBuilders
                .delete("/teams/{id}", 3152) // Use path variable
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // Act: Perform the HTTP request to delete the team
        ResultActions deleteResponse = mockMvc.perform(deleteBuilder);

        // Assert: Expect HTTP status OK (200) if the deletion is successful
        deleteResponse.andExpect(status().isOk());

        // Act: Attempt to retrieve the deleted team, expect 404 (Not Found) because the team is deleted
        MockHttpServletRequestBuilder getBuilder = MockMvcRequestBuilders
                .get("/teams/{id}",3152) // Use path variable
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // Assert: Expect 404 (Not Found) when trying to retrieve the deleted team
        ResultActions getResponse = mockMvc.perform(getBuilder);
        getResponse.andExpect(status().isNotFound());
    }

    /**
     * Test: Add a new team with a missing "name" field, expecting a bad request.
     *
     * @throws Exception the exception that may be thrown
     */
    /**
     * FAST Test: Add a new team with a missing "name" field, expecting a bad request.
     * Focused: This test focuses on handling a bad request when adding a team with a missing field.
     * Automated: It is automated to run with the test suite.
     * Small: It tests a specific feature - handling a bad request.
     * Testable: It's designed to be easily testable and maintainable.
     */

    @Test
    void addNewTeamMissingNameField() throws Exception {
        // Arrange: Create a new team associated with the existing warehouse
        Warehouse existingWarehouse = new Warehouse(3, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF", 100, 20, 0);
        Team team = new Team(null, existingWarehouse); // Missing "name" field

        // Act: Perform an HTTP request to add the new team without the "name" field
        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                // Assert: Expect a bad request status
                .andExpect(status().isBadRequest());
    }
        /**
         * Tests attempting to delete a non-existing team, expecting a 404 Not Found error.
         *
         * @throws Exception the exception
         */
    /**
     * FAST Test: Attempting to delete a non-existing team, expecting a 404 Not Found error.
     * Focused: This test focuses on handling a 404 error when deleting a non-existing team.
     * Automated: It is automated to run with the test suite.
     * Small: It tests a specific feature - handling a 404 error.
     * Testable: It's designed to be easily testable and maintainable.
     */
    @Test
    void deleteNonExistingTeam() throws Exception {
        // Attempt to delete a team with an invalid ID (e.g., ID = 999)
        MockHttpServletRequestBuilder deleteBuilder = MockMvcRequestBuilders
                .delete("/teams/{id}", 999) // Use a non-existing ID
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions deleteResponse = mockMvc.perform(deleteBuilder);
        deleteResponse.andExpect(status().isNotFound());  // Expect a 404 Not Found error
    }
}





