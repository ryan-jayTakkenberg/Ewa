package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.*;
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
class WarehouseControllerTest {

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
    void getWarehouses() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/warehouses")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    @Test
    void createWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse(0,"Test-name", "Amsterdam", "Bijlmer", "1234 EF");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String warehouseJson = objectMapper.writeValueAsString(warehouse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(warehouseJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isCreated(),
                jsonPath("$.id", greaterThan(0)),
                jsonPath("$.name").value(warehouse.getName()),
                jsonPath("$.city").value(warehouse.getCity()),
                jsonPath("$.address").value(warehouse.getAddress()),
                jsonPath("$.postalCode").value(warehouse.getPostalCode())
        );

        String responseJson = response.andReturn().getResponse().getContentAsString();
        warehouse = objectMapper.readValue(responseJson, Warehouse.class);

        assertNotNull(warehouse);
        assertTrue(warehouse.getId() > 0);
    }

    @Test
    void deleteWarehouse() throws Exception {
        long id = 3;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/warehouses/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").exists(),
                jsonPath("$.id").value(id)
        );
    }

    @Test
    void updateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse(1, "Test", "Amsterdam", "Bijlmer", "9012 AF");

        ObjectMapper objectMapper = new ObjectMapper();
        String warehouseJson = objectMapper.writeValueAsString(warehouse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/warehouses/" + warehouse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(warehouseJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isOk());
    }

    @Test
    void updateWarehouseInvalidData() throws Exception {
        Warehouse warehouse = new Warehouse(0, "Test", "Amsterdam", "Bijlmer", "9012 AF");

        ObjectMapper objectMapper = new ObjectMapper();
        String warehouseJson = objectMapper.writeValueAsString(warehouse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/warehouses/" + warehouse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(warehouseJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isNotFound());
    }
}
