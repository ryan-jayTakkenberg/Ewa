package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.Team;
import app.models.User;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static String token;
    private static User testUser;
    private static Team testTeam;
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
        // Create a test user
        testUser = new User();
        testUser.setPermissionLevel(PermissionLevel.ADMIN);
        testUser.setName("User Name");
        testUser.setEmail("user@email.com");
        testUser.setPassword("User Password");

        // Create a test team
        Team testTeam = new Team();
        testTeam.setId(1);
        testTeam.setName("Team Name");
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
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
    void createUserSuccessful() throws Exception {
        testUser.setId(NON_EXISTING_USER_ID);
        assertFalse(testUser.getId() > 0);
        testUser.setTeam(testTeam);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(testUser);

        System.out.println(userJson);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isCreated(),
                jsonPath("$.id", greaterThan(0)),
                jsonPath("$.permissionLevel").value(testUser.getPermissionLevel().name()),
                jsonPath("$.name").value(testUser.getName()),
                jsonPath("$.email").value(testUser.getEmail()),
                jsonPath("$.lastLogin").value(testUser.getLastLogin())
        );

        String responseJson = response.andReturn().getResponse().getContentAsString();
        testUser = objectMapper.readValue(responseJson, User.class);

        assertNotNull(testUser);
        assertTrue(testUser.getId() > 0);
    }

    @Test
    void createUserFail() throws Exception {
        testUser.setId(EXISTING_USER_ID);
        assertTrue(testUser.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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
    void updateUserFail() throws Exception {
        testUser.setId(NON_EXISTING_USER_ID);
        assertFalse(testUser.getId() > 0);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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
    void updateUserSuccessful() throws Exception {
        testUser.setId(EXISTING_USER_ID);
        assertTrue(testUser.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(EXISTING_USER_ID),
                jsonPath("$.permissionLevel").value(testUser.getPermissionLevel().name()),
                jsonPath("$.name").value(testUser.getName()),
                jsonPath("$.email").value(testUser.getEmail()),
                jsonPath("$.password").value(testUser.getPassword()),
                jsonPath("$.lastLogin").value(testUser.getLastLogin())
        );
    }

    @Test
    void deleteUserFail() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/users/" + NON_EXISTING_USER_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserSuccessful() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/users/" + EXISTING_USER_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").exists(),
                jsonPath("$.id").value(EXISTING_USER_ID)
        );
    }
}
