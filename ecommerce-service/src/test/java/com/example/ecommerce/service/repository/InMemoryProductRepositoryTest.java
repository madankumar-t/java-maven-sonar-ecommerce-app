package com.example.ecommerce.service.repository;

import com.example.ecommerce.common.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryProductRepositoryTest {

    private InMemoryProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    void savesAndFindsProduct() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        repository.save(product);

        assertTrue(repository.findById("p1").isPresent());
        assertEquals(product, repository.findById("p1").get());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        assertFalse(repository.findById("missing").isPresent());
    }

    @Test
    void findAllReturnsAllSavedProducts() {
        repository.save(new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics"));
        repository.save(new Product("p2", "Mouse", BigDecimal.ONE, 10, "Electronics"));

        List<Product> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void findByCategoryFiltersCorrectly() {
        repository.save(new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics"));
        repository.save(new Product("p2", "Shirt", BigDecimal.ONE, 10, "Clothing"));

        List<Product> electronics = repository.findByCategory("electronics");
        assertEquals(1, electronics.size());
        assertEquals("p1", electronics.get(0).getId());
    }

    @Test
    void deleteByIdRemovesProduct() {
        repository.save(new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics"));
        repository.deleteById("p1");
        assertFalse(repository.existsById("p1"));
    }

    @Test
    void existsByIdReturnsCorrectState() {
        assertFalse(repository.existsById("p1"));
        repository.save(new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics"));
        assertTrue(repository.existsById("p1"));
    }
}
