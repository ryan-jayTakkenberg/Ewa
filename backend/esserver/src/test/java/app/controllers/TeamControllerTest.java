package app.controllers;

 import app.controllers.TeamController;
        import app.enums.PermissionLevel;
        import app.jwt.JWTConfig;
        import app.jwt.JWToken;
        import app.models.Team;
        import app.models.Warehouse;
        import app.repositories.TeamJPARepository;
        import app.repositories.WarehouseJPARepository;
 import com.fasterxml.jackson.annotation.JsonFormat;
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
@Transactional
class TeamControllerTest {
    //resttemplate


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



        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name", "Test");
        teamJson.put("warehouseId", 3);

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

    @Test
    void updateTeam() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name", "test");
        teamJson.put("warehouseId",3);

        String teamJsonString = objectMapper.writeValueAsString(teamJson);

        // Create a team to update
        MockHttpServletRequestBuilder createBuilder = MockMvcRequestBuilders
                .post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(teamJsonString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions createResponse = mockMvc.perform(createBuilder);
        createResponse.andExpect(status().isCreated());


        MockHttpServletRequestBuilder updateBuilder = MockMvcRequestBuilders
                .put("/teams/" + 3)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions updateResponse = mockMvc.perform(updateBuilder);
        updateResponse.andExpect(status().isOk());

        // Retrieve the updated team
        MockHttpServletRequestBuilder getBuilder = MockMvcRequestBuilders
                .get("/teams/" + 3)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions getResponse = mockMvc.perform(getBuilder);
        getResponse.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Team Name"));
    }

    @Test
    void deleteTeam() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Create a JSON object with the required "warehouseId" field
        ObjectNode teamJson = objectMapper.createObjectNode();
        teamJson.put("name","Test Team");
        teamJson.put("warehouseId", 2);

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
                .delete("/teams/{id}", 2) // Use path variable
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions deleteResponse = mockMvc.perform(deleteBuilder);
        deleteResponse.andExpect(status().isOk());  // This should be 200 OK if the deletion is successful

        // Attempt to retrieve the deleted team, expect 404 (Not Found) because the team is deleted
        MockHttpServletRequestBuilder getBuilder = MockMvcRequestBuilders
                .get("/teams/{id}",2) // Use path variable
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions getResponse = mockMvc.perform(getBuilder);
        getResponse.andExpect(status().isNotFound());
    }
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




}


