package app.repositories;

import app.models.Product;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Repository.class, Entity.class }))
class ProductJPARepositoryTests {

    @Autowired
    private EntityRepositoryJPA<Product> productRepo;

    @Test
    void testRepoCRUD() {
        // create
        final String PRODUCT_NAME = "testName";
        Product product = new Product(PRODUCT_NAME, 0, "description");
        assertTrue(product.getId() <= 0);
        Product savedProduct = productRepo.save(product);
        assertTrue(savedProduct.getId() > 0);
        assertEquals(savedProduct.getName(), PRODUCT_NAME);

        // read
        assertEquals(productRepo.findAll(), List.of(savedProduct));
        assertEquals(productRepo.findById(savedProduct.getId()), savedProduct);

        // update
        final String UPDATED_PRODUCT_NAME = "updatedName";
        savedProduct.setName(UPDATED_PRODUCT_NAME);
        Product updatedProduct = productRepo.save(savedProduct);
        assertEquals(productRepo.findAll(), List.of(updatedProduct));
        assertEquals(productRepo.findById(updatedProduct.getId()), updatedProduct);
        assertEquals(updatedProduct.getName(), UPDATED_PRODUCT_NAME);
        assertNotEquals(updatedProduct.getName(), PRODUCT_NAME);

        // delete
        assertFalse(productRepo.findAll().isEmpty());
        Product deletedProduct = productRepo.delete(updatedProduct);
        assertNull(productRepo.findById(deletedProduct.getId()));
        assertTrue(productRepo.findAll().isEmpty());
    }

}
