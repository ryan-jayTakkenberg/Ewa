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
 * The type Team controller test.
 */
/**
 * The type Team controller test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String token;

    /**
     * Sets up the JWT token for authentication.
     */
    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        JWTConfig JwtConfig = new JWTConfig();
        JWToken jwt = new JWToken(-1, PermissionLevel.ADMIN, "127.0.0.1", JwtConfig.getExpiration());
        token = jwt.encode(JwtConfig.getIssuer(), JwtConfig.getPassphrase());

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    /**
     * Verifies successful autowiring of MockMvc.
     */
    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
    }

    /**
     * Tests the retrieval of teams.
     *
     * @throws Exception the exception
     */
    @Test
    void getTeams() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/teams")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    /**
     * Tests adding a new team.
     *
     * @throws Exception the exception
     */
    @Test
    void addNewTeam() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name", "Test");
        teamJson.put("warehouseId", 5);

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test"));

    }

    /**
     * Tests deleting a team.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteTeam() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name","Team Blue");
        teamJson.put("warehouseId", 5);

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        // Create a team to delete
        MockHttpServletRequestBuilder createBuilder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions createResponse = mockMvc.perform(createBuilder);
        createResponse.andExpect(status().isCreated());

        MockHttpServletRequestBuilder deleteBuilder = MockMvcRequestBuilders
                .delete("/teams/{id}", 503) // Use path variable
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions deleteResponse = mockMvc.perform(deleteBuilder);
        deleteResponse.andExpect(status().isOk());  // This should be 200 OK if the deletion is successful

        // Attempt to retrieve the deleted team, expect 404 (Not Found) because the team is deleted
        MockHttpServletRequestBuilder getBuilder = MockMvcRequestBuilders
                .get("/teams/{id}",5) // Use path variable
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions getResponse = mockMvc.perform(getBuilder);
        getResponse.andExpect(status().isNotFound());
    }

    /**
     * Tests adding a new team with a missing name field, expecting a bad request.
     *
     * @throws Exception the exception
     */
    @Test
    void addNewTeamMissingNameField() throws Exception {
        // Create a new team associated with the existing warehouse
        Warehouse existingWarehouse = new Warehouse(3, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF", 100, 20, 0);
        Team team = new Team(null, existingWarehouse); // Missing "name" field

        // Use Spring's content method to convert the team object to JSON
        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests attempting to delete a non-existing team, expecting a 404 Not Found error.
     *
     * @throws Exception the exception
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





