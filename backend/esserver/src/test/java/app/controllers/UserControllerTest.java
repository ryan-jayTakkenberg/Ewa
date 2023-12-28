package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.*;
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


import java.time.LocalDate;
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

    private static Team testTeam;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        // Create a test warehouse
        Warehouse testWarehouse = new Warehouse(1, "Test Warehouse", "city", "address", "0000AM");

        // Create a test team
        testTeam = new Team(PermissionLevel.ADMIN, 1, "Test Team", testWarehouse);

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
    void createUserSuccessful() throws Exception {
        User user = new User(PermissionLevel.ADMIN, "full name", "email@mail.com", null, "password", null);

        assertFalse(user.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isCreated(),
                jsonPath("$.id", greaterThan(0)),
                jsonPath("$.permissionLevel").value(user.getPermissionLevel().name()),
                jsonPath("$.name").value(user.getName()),
                jsonPath("$.email").value(user.getEmail()),
                jsonPath("$.lastLogin").value(user.getLastLogin())
        );

        String responseJson = response.andReturn().getResponse().getContentAsString();
        user = objectMapper.readValue(responseJson, User.class);

        assertNotNull(user);
        assertTrue(user.getId() > 0);
    }

    @Test
    void createUserFail() throws Exception {
        User user = new User(PermissionLevel.ADMIN, "full name", "email@mail.com", LocalDate.of(2023, 10, 1), "password", testTeam);
        user.setId(1);

        assertTrue(user.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        response.andExpect(status().isBadRequest());
    }


    @Test
    void getUsers() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    @Test
    void updateUserFail() throws Exception {
        final long ID = -1;

        User user = new User(PermissionLevel.ADMIN, "full name", "email@mail.com", LocalDate.of(2023, 10, 1), "password", testTeam );
        user.setId(ID);

        assertFalse(user.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(user);

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
        final long ID = 1;

        User user = new User(PermissionLevel.ADMIN, "full name", "email@mail.com", LocalDate.of(2023, 10, 1), "password", testTeam );
        user.setId(ID);

        assertTrue(user.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(ID),
                jsonPath("$.permissionLevel").value(user.getPermissionLevel().name()),
                jsonPath("$.name").value(user.getName()),
                jsonPath("$.email").value(user.getEmail()),
                jsonPath("$.password").value(user.getPassword()),
                jsonPath("$.lastLogin").value(user.getLastLogin().toString())
        );
    }

    @Test
    void deleteUserFail() throws Exception {
        final long ID = -1;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/users/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserSuccessful() throws Exception {
        final long ID = 2;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/users/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").exists(),
                jsonPath("$.id").value(ID)
        );
    }
}
