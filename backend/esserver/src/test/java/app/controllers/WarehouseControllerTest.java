package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.*;
import app.repositories.WarehouseJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseJPARepository warehouseRepository;

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

    //F.I.R.S.t
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
    void getWarehouseByIdInvalidId() throws Exception {
        //arrange
        long invalidId = 999;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/warehouses/{id}", invalidId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        //act
        mockMvc.perform(builder)
                //assert
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWarehouseInvalidData() throws Exception {
        //arrange
        Warehouse warehouse = new Warehouse(0, "Test", "Amsterdam", "Bijlmer", "9012 AF", 100, 20, 0);

        ObjectMapper objectMapper = new ObjectMapper();
        String warehouseJson = objectMapper.writeValueAsString(warehouse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/warehouses/" + warehouse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(warehouseJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        //act
        ResultActions response = mockMvc.perform(builder);
        //assert
        response.andExpect(status().isNotFound());
    }

    @Test
    void deleteWarehouse() throws Exception {
        //arrange
        long id = 3;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/warehouses/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        //act
        ResultActions response = mockMvc.perform(builder);
        //assert
        response.andExpectAll(
                status().isOk()
        );
    }

    @Test
    void addNewWarehouse() throws Exception {
        //arrange
        Warehouse newWarehouse = new Warehouse(0, "New Warehouse", "New Address", "New City", "12345", 200, 50, 0);
        ObjectMapper objectMapper = new ObjectMapper();
        String newWarehouseJson = objectMapper.writeValueAsString(newWarehouse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newWarehouseJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        //act
        mockMvc.perform(builder)
                //assert
                .andExpect(status().isOk());
    }
}
