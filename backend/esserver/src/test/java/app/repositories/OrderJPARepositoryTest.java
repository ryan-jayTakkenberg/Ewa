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


}
