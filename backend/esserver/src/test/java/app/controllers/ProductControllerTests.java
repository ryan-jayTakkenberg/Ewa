package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
class ProductControllerTests {

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
    void createProductFail() throws Exception {
        Product product = new Product("product name", 100, "product description");
        product.setId(1);

        assertTrue(product.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(product);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }

    @Test
    void createProductSuccessful() throws Exception {
        Product product = new Product("product name", 100, "product description");

        assertFalse(product.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(product);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isCreated(),
                jsonPath("$.id", greaterThan(0)),
                jsonPath("$.name").value(product.getName()),
                jsonPath("$.price").value(product.getPrice()),
                jsonPath("$.description").value(product.getDescription())
        );

        String responseJson = response.andReturn().getResponse().getContentAsString();
        product = objectMapper.readValue(responseJson, Product.class);

        assertNotNull(product);
        assertTrue(product.getId() > 0);
    }

    @Test
    void getProducts() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/product")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    @Test
    void getProductFail() throws Exception {
        final long ID = -1;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/product/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isNotFound());
    }

    @Test
    void getProductSuccessful() throws Exception {
        final long ID = 1;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/product/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").exists(),
                jsonPath("$.id").value(ID)
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = response.andReturn().getResponse().getContentAsString();
        Product product = objectMapper.readValue(responseJson, Product.class);

        assertNotNull(product);
        assertEquals(product.getId(), ID);
    }

    @Test
    void updateProductFail() throws Exception {
        final long ID = -1;

        Product product = new Product("product name", 100, "product description");
        product.setId(ID);

        assertFalse(product.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(product);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isNotFound());
    }

    @Test
    void updateProductSuccessful() throws Exception {
        final long ID = 1;

        Product product = new Product("product name", 100, "product description");
        product.setId(ID);

        assertTrue(product.getId() > 0);

        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(product);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").exists(),
                jsonPath("$.id").value(ID),
                jsonPath("$.name").value(product.getName()),
                jsonPath("$.price").value(product.getPrice()),
                jsonPath("$.description").value(product.getDescription())
        );
    }

    @Test
    void deleteProductFail() throws Exception {
        final long ID = -1;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/product/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }

    @Test
    void deleteProductSuccessful() throws Exception {
        final long ID = 2;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/product/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").exists(),
                jsonPath("$.id").value(ID)
        );
    }
}
