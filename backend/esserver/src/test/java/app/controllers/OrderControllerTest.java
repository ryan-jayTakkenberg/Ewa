package app.controllers;

import app.enums.PermissionLevel;
import app.jwt.JWTConfig;
import app.jwt.JWToken;
import app.models.*;
import app.models.Order;
import app.repositories.OrderJPARepository;
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
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    void postOrderSuccessful() throws Exception {
        // Mocking data
        Product mockProduct = new Product();
        mockProduct.setName("product Name");
        mockProduct.setPrice(100);
        mockProduct.setDescription("desc");
        Product savedProduct = productRepository.save(mockProduct);

        Team mockTeam = new Team();
        mockTeam.setName("Mock Team");
        Team savedTeam = teamRepository.save(mockTeam);

        // JSON payload for the request
        String orderJson = "{"
                + "\"name\":\"TestOrder\","
                + "\"orderedFrom\":\"TestLocation\","
                + "\"status\":\"PENDING\","
                + "\"orderDate\":\"2022-01-01\","
                + "\"estimatedDeliveryDate\":\"2022-01-10\","
                + "\"teamId\":" + savedTeam.getId() + ","
                + "\"products\":[{\"amount\":2,\"productId\":" + savedProduct.getId() + "}]"
                + "}";

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value("TestOrder"))
                .andExpect(jsonPath("$.orderedFrom").value("TestLocation"))
                .andExpect(jsonPath("$.orderDate").value("2022-01-01"))
                .andExpect(jsonPath("$.estimatedDeliveryDate").value("2022-01-10"))
                .andExpect(jsonPath("$.team.id").value(savedTeam.getId()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.orderedProducts[0].product.id").value(savedProduct.getId()))
                .andDo(print());
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

//    @Test
//    void putOrderSuccessful() throws Exception {
//        Order order = new Order("Order name", "Ordered From", LocalDate.now(), LocalDate.now().plusDays(7), testTeam, Set.of(), Order.OrderStatus.PENDING);
//        Order savedOrder = orderRepository.save(order);
//        assertTrue(savedOrder.getId() > 0);
//        savedOrder.setName("New name");
//        savedOrder.setEstimatedDeliveryDate(LocalDate.now().plusDays(12));
//
//        // Adding new products to the order
//        Product newProduct1 = new Product("New Product 1", 15.0, "New Description 1");
//        Product newProduct2 = new Product("New Product 2", 25.0, "New Description 2");
//        Product savedProduct1 = productRepository.save(newProduct1);
//        Product savedProduct2 = productRepository.save(newProduct2);
//
//        // Create JSON payload for the request
//        String orderJson = "{"
//                + "\"id\":" + savedOrder.getId() + ","
//                + "\"name\":\"" + savedOrder.getName() + "\","
//                + "\"orderedFrom\":\"" + savedOrder.getOrderedFrom() + "\","
//                + "\"orderDate\":\"" + savedOrder.getOrderDate() + "\","
//                + "\"estimatedDeliveryDate\":\"" + savedOrder.getEstimatedDeliveryDate() + "\","
//                + "\"teamId\":" + savedOrder.getTeam().getId() + ","
//                + "\"status\":\"" + savedOrder.getStatus() + "\","
//                + "\"products\":["
//                + "{\"amount\":3,\"productId\":" + savedProduct1.getId() + "},"
//                + "{\"amount\":5,\"productId\":" + savedProduct2.getId() + "}"
//                + "]"
//                + "}";
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
//                .put("/orders")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(orderJson)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
//
//        ResultActions response = mockMvc.perform(builder);
//        response.andExpectAll(
//                status().isOk(),
//                jsonPath("$.id").value(savedOrder.getId()),
//                jsonPath("$.name").value(savedOrder.getName()),
//                jsonPath("$.orderedFrom").value(savedOrder.getOrderedFrom()),
//                jsonPath("$.orderDate").value(savedOrder.getOrderDate().toString()),
//                jsonPath("$.estimatedDeliveryDate").value(savedOrder.getEstimatedDeliveryDate().toString()),
//                jsonPath("$.team.id").value(savedOrder.getTeam().getId()),
//                jsonPath("$.status").value(savedOrder.getStatus().toString()),
//                jsonPath("$.orderedProducts[0].amount").value(3),
//                jsonPath("$.orderedProducts[1].amount").value(5),
//                jsonPath("$.orderedProducts[0].product.id").value(savedProduct1.getId()),
//                jsonPath("$.orderedProducts[1].product.id").value(savedProduct2.getId())
//        );
//    }
    @Test
    void deleteOrderFail() throws Exception {
        final long ID = -1;

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/orders/" + ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ResultActions response = mockMvc.perform(builder);
        response.andExpect(status().isBadRequest());
    }

}
