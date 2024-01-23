package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.Team;
import app.models.User;
import app.repositories.TeamJPARepository;
import app.repositories.UserJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Locale;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamJPARepository teamRepository;
    @Autowired
    private UserJPARepository userRepository;
    private static String token;
    private static User testUser;
    private static Team testTeam;
    private static ObjectMapper objectMapper;
    private static final long EXISTING_USER_ID = 1;
    private static final long NON_EXISTING_USER_ID = 0;

    @BeforeAll
    static void setupAll() {
        Locale.setDefault(Locale.ENGLISH);
        JWTConfig JwtConfig = new JWTConfig();
        JWToken jwt = new JWToken(-1, PermissionLevel.ADMIN, "127.0.0.1", JwtConfig.getExpiration());
        token = jwt.encode(JwtConfig.getIssuer(), JwtConfig.getPassphrase());
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @BeforeEach
    void setupEach() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For use LocalDate in mock data

        // Create and save test team
        testTeam = new Team();
        testTeam.setId(1);
        testTeam.setName("Team Name");

        // Create a test user
        testUser = new User();
        testUser.setPermissionLevel(PermissionLevel.ADMIN);
        testUser.setName("User Name");
        testUser.setEmail("user@email.com");
        testUser.setPassword("User Password");
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
        assertNotNull(teamRepository);
    }

    @Test
    void getUsers() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    @Test
    void postUserSuccessful() throws Exception {
        // Create a test team
        Team testTeam = new Team();
        testTeam.setName("Team Name");
        Team savedTeam = teamRepository.save(testTeam);

        // JSON payload for the request
        String userJson = "{"
                + "\"name\":\"New User\","
                + "\"permissionLevel\":\"ADMIN\","
                + "\"email\":\"newuser@email.com\","
                + "\"password\":\"password-test\","
                + "\"teamId\":" + savedTeam.getId()
                + "}";

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.permissionLevel").value("ADMIN"))
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("newuser@email.com"))
                .andDo(print());

        String responseJson = response.andReturn().getResponse().getContentAsString();
        testUser = objectMapper.readValue(responseJson, User.class);

        assertNotNull(testUser);
        assertTrue(testUser.getId() > 0);

    }


    @Test
    void postUserFail() throws Exception {
        testUser.setId(EXISTING_USER_ID);
        assertTrue(testUser.getId() > 0);

        String userJson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        response.andExpect(status().isBadRequest());
    }

    @Test
    void putUserSuccessful() throws Exception {
        // Create a test team
        Team testTeam = new Team();
        testTeam.setName("Team Name");
        Team savedTeam = teamRepository.save(testTeam);

        // Create a test user
        User testUser = new User();
        testUser.setName("Existing User");
        testUser.setPermissionLevel(PermissionLevel.VIEWER);
        testUser.setEmail("existinguser@email.com");
        testUser.setPassword("existing-password");
        testUser.setTeam(savedTeam);
        User savedUser = userRepository.save(testUser);

        // Updated user details
        String updatedName = "Updated User";
        String updatedEmail = "updateduser@email.com";

        // JSON payload for the request
        String updatedUserJson = "{"
                + "\"userId\":" + savedUser.getId() + ","
                + "\"name\":\"" + updatedName + "\","
                + "\"permissionLevel\":\"ADMIN\","
                + "\"email\":\"" + updatedEmail + "\","
                + "\"password\":\"updated-password\","
                + "\"teamId\":" + savedTeam.getId()
                + "}";

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedUserJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.permissionLevel").value("ADMIN"))
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.email").value(updatedEmail))
                .andDo(print());
    }

    @Test
    void putUserFail() throws Exception {
        testUser.setId(NON_EXISTING_USER_ID);
        assertFalse(testUser.getId() > 0);
        String userJson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isNotFound());
    }
    @Test
    void deleteUserSuccessful() throws Exception {
        // Create a test team
        Team testTeam = new Team();
        testTeam.setName("Team Name");
        Team savedTeam = teamRepository.save(testTeam);

        // Create a test user
        User testUser = new User();
        testUser.setName("User to be deleted");
        testUser.setPermissionLevel(PermissionLevel.VIEWER);
        testUser.setEmail("deleteuser@email.com");
        testUser.setPassword("delete-password");
        testUser.setTeam(savedTeam);
        User savedUser = userRepository.save(testUser);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/users/" + savedUser.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.permissionLevel").value("VIEWER"))
                .andExpect(jsonPath("$.name").value("User to be deleted"))
                .andExpect(jsonPath("$.email").value("deleteuser@email.com"))
                .andDo(print());
    }


    @Test
    void deleteUserFail() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/users/" + NON_EXISTING_USER_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }
}
