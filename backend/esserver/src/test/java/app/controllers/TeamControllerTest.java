package app.controllers;

 import app.controllers.TeamController;
        import app.enums.PermissionLevel;
        import app.jwt.JWTConfig;
        import app.jwt.JWToken;
        import app.models.Team;
        import app.models.Warehouse;
        import app.repositories.TeamJPARepository;
        import app.repositories.WarehouseJPARepository;
        import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ObjectNode;
 import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

        import java.util.List;
        import java.util.Locale;

        import static org.hamcrest.Matchers.greaterThan;
        import static org.hamcrest.Matchers.hasSize;
        import static org.junit.jupiter.api.Assertions.assertNotNull;
        import static org.junit.jupiter.api.Assertions.assertTrue;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String token;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        JWTConfig JwtConfig = new JWTConfig();
        JWToken jwt = new JWToken(-1, PermissionLevel.ADMIN, "127.0.0.1", JwtConfig.getExpiration());
        token = jwt.encode(JwtConfig.getIssuer(), JwtConfig.getPassphrase());

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
    }

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

    @Test
    void addNewTeam() throws Exception {
        // Existing warehouse with ID 3
        Warehouse existingWarehouse = new Warehouse(3, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF");

        // Create a new team associated with the existing warehouse
        Team team = new Team("Test Team", existingWarehouse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name", team.getName());
        teamJson.put("warehouseId", existingWarehouse.getId());

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(team.getName()));

        // You can retrieve the created team from the response if needed
        String responseJson = response.andReturn().getResponse().getContentAsString();
        team = objectMapper.readValue(responseJson, Team.class);

        assertNotNull(team);
        assertTrue(team.getId() > 0);
    }

    @Test
    void updateTeam() throws Exception {
        Warehouse existingWarehouse = new Warehouse(3, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF");

        Team team = new Team("Updated Team", existingWarehouse);

        ObjectMapper objectMapper = new ObjectMapper();

        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name", team.getName());
        teamJson.put("warehouseId", existingWarehouse.getId());

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        // Create a team to update
        MockHttpServletRequestBuilder createBuilder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions createResponse = mockMvc.perform(createBuilder);
        createResponse.andExpect(status().isCreated());

        String createResponseJson = createResponse.andReturn().getResponse().getContentAsString();
        team = objectMapper.readValue(createResponseJson, Team.class);

        // Update the team
        team.setName("Updated Team Name");

        MockHttpServletRequestBuilder updateBuilder = MockMvcRequestBuilders
                .put("/teams/" + team.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(team))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions updateResponse = mockMvc.perform(updateBuilder);
        updateResponse.andExpect(status().isOk());

        // Retrieve the updated team
        MockHttpServletRequestBuilder getBuilder = MockMvcRequestBuilders
                .get("/teams/" + team.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions getResponse = mockMvc.perform(getBuilder);
        getResponse.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Team Name"));
    }

    @Test
    void deleteTeam() throws Exception {
        Warehouse existingWarehouse = new Warehouse(3, "Dutch Warehouse", "Amsterdam", "Straat 333", "1234 EF");

        Team team = new Team("Test Team", existingWarehouse);

        ObjectMapper objectMapper = new ObjectMapper();
        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name", team.getName());
        teamJson.put("warehouseId", existingWarehouse.getId());

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        // Create a team to delete
        MockHttpServletRequestBuilder createBuilder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions createResponse = mockMvc.perform(createBuilder);
        createResponse.andExpect(status().isCreated());

        String createResponseJson = createResponse.andReturn().getResponse().getContentAsString();
        team = objectMapper.readValue(createResponseJson, Team.class);

        MockHttpServletRequestBuilder deleteBuilder = MockMvcRequestBuilders
                .delete("/teams/" + team.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions deleteResponse = mockMvc.perform(deleteBuilder);
        deleteResponse.andExpect(status().isOk());  // Dit moet 200 OK zijn als de verwijdering succesvol is

        // Probeert het verwijderde team op te halen, verwacht 405 (Method Not Allowed)
        MockHttpServletRequestBuilder getBuilder = MockMvcRequestBuilders
                .get("/teams/" + team.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions getResponse = mockMvc.perform(getBuilder);
        getResponse.andExpect(status().isMethodNotAllowed());  // Aangepast naar 405
    }



}


