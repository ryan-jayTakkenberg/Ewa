package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.*;
import app.models.Order;
import app.models.relations.Product_Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
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
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String token;

    private static Warehouse testWarehouse;

    private static Team testTeam;
    private static Product testProduct;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);
        // Create a test product
        testProduct = new Product("name", 12, "description");
        // Create a test warehouse
        testWarehouse = new Warehouse(1, "Test Warehouse", "city", "address", "0000AM");
        // Create a test team
        testTeam = new Team(PermissionLevel.ADMIN, 1, "Test Team", testWarehouse);

        JWTConfig JwtConfig = new JWTConfig();
        JWToken jwt = new JWToken(-1, PermissionLevel.ADMIN, "127.0.0.1", JwtConfig.getExpiration());
        token = jwt.encode(JwtConfig.getIssuer(), JwtConfig.getPassphrase());

        assertNotNull(token);
        assertTrue(token.length() > 0);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
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
    void createOrderSuccessful() throws Exception {
        Order order = new Order("Order name", "Ordered From", LocalDate.now(),
                LocalDate.now().plusDays(7), testTeam, new HashSet<>(), Order.OrderStatus.PENDING);
        Product_Order productOrder = new Product_Order(2, testProduct, null);

        order.addProductOrder(productOrder);

        assertFalse(order.getId() > 0);

        objectMapper.registerModule(new JavaTimeModule());
        String orderJson = objectMapper.writeValueAsString(order);

        System.out.println("Order JSON: " + orderJson); // Print the JSON content

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        System.out.println(response); // Print the response
        response.andExpectAll(
                status().isCreated(),
                jsonPath("$.id", greaterThan(0)),
                jsonPath("$.name").value(order.getName()),
                jsonPath("$.orderedFrom").value(order.getOrderedFrom()),
                jsonPath("$.orderDate").value(order.getOrderDate().toString()),
                jsonPath("$.estimatedDeliveryDate").value(order.getEstimatedDeliveryDate().toString()),
                jsonPath("$.team.id").value(order.getTeam().getId()),
                jsonPath("$.products[0].product.name").value(order.getProducts().iterator().next().getProduct().getName()),
                jsonPath("$.status").value(order.getStatus().toString())
        );

        String responseJson = response.andReturn().getResponse().getContentAsString();
        order = objectMapper.readValue(responseJson, Order.class);

        assertNotNull(order);
        assertTrue(order.getId() > 0);
    }


    @Test
    void createOrderFail() throws Exception {
        Product product = new Product("name", 120, "description");
        Product_Order orderedProduct = new Product_Order(2, product, null);
        Order order = new Order("Order name", "Ordered From", LocalDate.now(), LocalDate.now().plusDays(7), testTeam, Set.of(orderedProduct), Order.OrderStatus.PENDING);

        order.setId(1);
        assertTrue(order.getId() > 0);

        objectMapper.registerModule(new JavaTimeModule());
        String orderJson = objectMapper.writeValueAsString(order);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);

        response.andExpect(status().isBadRequest());
    }


    @Test
    void getOrders() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").isArray()
        );
    }

    @Test
    void updateOrderFail() throws Exception {
        final long ID = -1;

        Product product = new Product("name", 120, "description");
        Product_Order orderedProduct = new Product_Order(2, product, null);
        Order order = new Order("Order name", "Ordered From", LocalDate.now(), LocalDate.now().plusDays(7), testTeam, Set.of(orderedProduct), Order.OrderStatus.PENDING);
        order.setId(ID);

        assertFalse(order.getId() > 0);

        objectMapper.registerModule(new JavaTimeModule());
        String OrderJson = objectMapper.writeValueAsString(order);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OrderJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isNotFound());
    }

    @Test
    void updateOrderSuccessful() throws Exception {
        final long ID = 1;

        Product product = new Product("name", 120, "description");
        Product_Order orderedProduct = new Product_Order(2, product, null);
        Order order = new Order("Order name", "Ordered From", LocalDate.now(), LocalDate.now().plusDays(7), testTeam, Set.of(orderedProduct), Order.OrderStatus.PENDING);
        order.setId(ID);

        assertTrue(order.getId() > 0);

        objectMapper.registerModule(new JavaTimeModule());
        String OrderJson = objectMapper.writeValueAsString(order);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OrderJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(ID),
                jsonPath("$.name").value(order.getName()),
                jsonPath("$.orderedFrom").value(order.getOrderedFrom()),
                jsonPath("$.orderDate").value(order.getOrderDate()),
                jsonPath("$.estimatedDeliveryDate").value(order.getEstimatedDeliveryDate()),
                jsonPath("$.team.id").value(order.getTeam().getId()),
                jsonPath("$.products[0].product.name").value(order.getProducts().iterator().next().getProduct().getName()),
                jsonPath("$.status").value(order.getStatus())
        );
    }

    @Test
    void deleteOrderFail() throws Exception {
        final long ID = -1;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/orders/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }

    @Test
    void deleteOrderSuccessful() throws Exception {
        final long ID = 2;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/orders/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$").exists(),
                jsonPath("$.id").value(ID)
        );
    }

    private Product createProduct(String name, int price, String description) throws Exception {
        Product product = new Product(name, price, description);
        String productJson = objectMapper.writeValueAsString(product);

        MockHttpServletRequestBuilder productBuilder = MockMvcRequestBuilders
                .post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions productResponse = mockMvc.perform(productBuilder);
        productResponse.andExpect(status().isCreated());

        String productResponseJson = productResponse.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(productResponseJson, Product.class);
    }
}
