package app.repositories;

import app.models.Order;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Repository.class, Entity.class}))
class OrderJPARepositoryTest {

    @Autowired
    private EntityRepositoryJPA<Order> orderRepo;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(orderRepo);
    }

    @Test
    void testRepoCRUD() {
        // create
        final String ORDER_NAME = "orderName";
        Order order = new Order();
        order.setName(ORDER_NAME);
        assertTrue(order.getId() <= 0);
        Order savedOrder = orderRepo.save(order);
        assertTrue(savedOrder.getId() > 0);
        assertEquals(savedOrder.getName(), ORDER_NAME);

        // read
        assertEquals(orderRepo.findAll(), List.of(savedOrder));
        assertEquals(orderRepo.findById(savedOrder.getId()), savedOrder);

        // update
        final String UPDATED_PRODUCT_NAME = "updatedName";
        savedOrder.setName(UPDATED_PRODUCT_NAME);
        Order updatedOrder = orderRepo.save(savedOrder);
        assertEquals(orderRepo.findAll(), List.of(updatedOrder));
        assertEquals(orderRepo.findById(updatedOrder.getId()), updatedOrder);
        assertEquals(updatedOrder.getName(), UPDATED_PRODUCT_NAME);
        assertNotEquals(updatedOrder.getName(), ORDER_NAME);

        // delete
        assertFalse(orderRepo.findAll().isEmpty());
        Order deletedOrder = orderRepo.delete(updatedOrder);
        assertNull(orderRepo.findById(deletedOrder.getId()));
        assertTrue(orderRepo.findAll().isEmpty());
    }
}
