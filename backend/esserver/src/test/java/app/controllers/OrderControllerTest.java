package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.*;
import app.models.Order;
import app.models.relations.Product_Order;
import app.repositories.OrderJPARepository;
import app.repositories.ProductJPARepository;
import app.repositories.TeamJPARepository;
import app.repositories.WarehouseJPARepository;
import com.fasterxml.jackson.databind.JsonNode;
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
    @Autowired
    private WarehouseJPARepository warehouseRepository;

    @Autowired
    private TeamJPARepository teamRepository;

    @Autowired
    private ProductJPARepository productRepository;

    @Autowired
    private OrderJPARepository orderRepository;

    private static ObjectMapper objectMapper;

    private static Team testTeam;
    private static Product testProduct;
    private static Warehouse testWarehouse;


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
        assertNotNull(warehouseRepository);
        assertNotNull(teamRepository);
        assertNotNull(productRepository);
        assertNotNull(orderRepository);
    }

    @BeforeEach
    void setupTest() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create test product, team and warehouse
        testProduct = new Product("Product 1", 10.0, "Description 1");
        testWarehouse = new Warehouse(1, "Test Warehouse", "city", "address", "0000AM", 1, 1, 1);
        testTeam = new Team("Team name", testWarehouse);

        testProduct = productRepository.save(testProduct);
        testWarehouse = warehouseRepository.save(testWarehouse);
        testTeam = teamRepository.save(testTeam);
    }

    @Test
    void createOrderSuccessful() throws Exception {
        // Create an order
        Order order = new Order();
        order.setName("Order name");
        order.setOrderedFrom("Ordered From");
        order.setOrderDate(LocalDate.now());
        order.setEstimatedDeliveryDate(LocalDate.now().plusDays(7));
        order.setTeam(testTeam);
        order.setStatus(Order.OrderStatus.PENDING);

        assertFalse(order.getId() > 0);

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
                jsonPath("$.team.id").value(testTeam.getId()), // Validate the team ID
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
        Order order = new Order("Order name", "Ordered From", LocalDate.now(), LocalDate.now().plusDays(7), testTeam, Set.of(), Order.OrderStatus.PENDING);

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

        Product product = new Product("Product 1", 10.0, "Description 1");
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
        Order OldOrder = new Order("Order name", "Ordered From", LocalDate.now(), LocalDate.now().plusDays(7), testTeam, Set.of(), Order.OrderStatus.PENDING);
        Order newOrder = orderRepository.save(OldOrder);
        assertTrue(newOrder.getId() > 0);

        newOrder.setName("New name");
        newOrder.setEstimatedDeliveryDate(LocalDate.now().plusDays(12));

        String OrderJson = objectMapper.writeValueAsString(newOrder);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OrderJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(newOrder.getId()),
                jsonPath("$.name").value(newOrder.getName()),
                jsonPath("$.orderedFrom").value(newOrder.getOrderedFrom()),
                jsonPath("$.orderDate").value(newOrder.getOrderDate().toString()),
                jsonPath("$.estimatedDeliveryDate").value(newOrder.getEstimatedDeliveryDate().toString()),
                jsonPath("$.team.id").value(newOrder.getTeam().getId()),
                jsonPath("$.status").value(newOrder.getStatus().toString())
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

    @Test
    void addProductOrdersToOrder() throws Exception {
        // Create an order
        Order order = new Order();
        order.setName("Order with Products");
        order.setOrderedFrom("Ordered From");
        order.setOrderDate(LocalDate.now());
        order.setEstimatedDeliveryDate(LocalDate.now().plusDays(7));
        order.setTeam(testTeam);
        order.setStatus(Order.OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        Product product1 = new Product("Product 1", 10.0, "Description 1");
        Product product2 = new Product("Product 2", 20.0, "Description 2");
        Product savedProduct1 = productRepository.save(product1);
        Product savedProduct2 = productRepository.save(product2);

        JsonNode orderedProductJson1 = objectMapper.createObjectNode()
                .put("amount", 2)
                .put("productId", savedProduct1.getId())
                .put("orderId", savedOrder.getId());

        JsonNode orderedProductJson2 = objectMapper.createObjectNode()
                .put("amount", 2)
                .put("productId", savedProduct2.getId())
                .put("orderId", savedOrder.getId());

        // Save Product_Order objects
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/product_order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderedProductJson1))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/product_order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderedProductJson2))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isCreated());

        // Retrieve the order with associated product_orders
        ResultActions getOrderWithProductsResponse = mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders" + "/" + order.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        String getOrderWithProductsJson = getOrderWithProductsResponse.andReturn().getResponse().getContentAsString();
        Order orderWithProducts = objectMapper.readValue(getOrderWithProductsJson, Order.class);

        assertNotNull(orderWithProducts.getOrderedProducts());
        assertFalse(orderWithProducts.getOrderedProducts().isEmpty());
        assertEquals(2, orderWithProducts.getOrderedProducts().size());
    }

}
