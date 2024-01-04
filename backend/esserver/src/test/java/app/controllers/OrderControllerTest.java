package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.*;
import app.models.Order;
import app.models.relations.Product_Order;
import app.repositories.ProductJPARepository;
import app.repositories.TeamJPARepository;
import app.repositories.WarehouseJPARepository;
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

    @Autowired
    private WarehouseJPARepository warehouseRepository;

    @Autowired
    private TeamJPARepository teamRepository;

    @Autowired
    private ProductJPARepository productRepository;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);

        JWTConfig JwtConfig = new JWTConfig();
        JWToken jwt = new JWToken(-1, PermissionLevel.ADMIN, "127.0.0.1", JwtConfig.getExpiration());
        token = jwt.encode(JwtConfig.getIssuer(), JwtConfig.getPassphrase());

        assertNotNull(token);
        assertTrue(token.length() > 0);

        // Create test product, team and warehouse
        testProduct = new Product("name", 12, "description");
        testWarehouse = new Warehouse(1, "Test Warehouse", "city", "address", "0000AM");
        testTeam = new Team(PermissionLevel.ADMIN, 1, "Test Team", testWarehouse);
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(mockMvc);
    }

    @Test
    void createOrderSuccessful() throws Exception {
        // Create an order
        Order order = new Order();
        order.setId(0);
        order.setName("Order name");
        order.setOrderedFrom("Ordered From");
        order.setOrderDate(LocalDate.now());
        order.setEstimatedDeliveryDate(LocalDate.now().plusDays(7));
        order.setTeam(testTeam);
        order.setStatus(Order.OrderStatus.PENDING);
        order.addOrderedProduct(2, testProduct); // Add a product order to the order
        assertFalse(order.getId() > 0);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Convert the order object to JSON
        String orderJson = objectMapper.writeValueAsString(order);

        // Prepare the request
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // Perform the request and validate the response
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
                jsonPath("$.products[0].product.name").value(order.getName()),
                jsonPath("$.status").value(order.getStatus().toString())
        );

        // Parse the response JSON to an Order object
        String responseJson = response.andReturn().getResponse().getContentAsString();
        order = objectMapper.readValue(responseJson, Order.class);

        // Validate that the order is not null and has a valid ID
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
//                jsonPath("$.products[0].product.name").value(order.getProducts().iterator().next().getProduct().getName()),
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
}
