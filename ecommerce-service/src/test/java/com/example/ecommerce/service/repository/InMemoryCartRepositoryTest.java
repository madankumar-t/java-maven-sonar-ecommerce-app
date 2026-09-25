package com.example.ecommerce.service.repository;

import com.example.ecommerce.common.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class InMemoryCartRepositoryTest {

    private InMemoryCartRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCartRepository();
    }

    @Test
    void findOrCreateCreatesNewCartWhenMissing() {
        Cart cart = repository.findOrCreate("c1");
        assertEquals("c1", cart.getCustomerId());
    }

    @Test
    void findOrCreateReturnsSameCartOnSubsequentCalls() {
        Cart first = repository.findOrCreate("c1");
        Cart second = repository.findOrCreate("c1");
        assertSame(first, second);
    }

    @Test
    void saveReplacesExistingCart() {
        Cart cart = new Cart("c1");
        repository.save(cart);
        assertSame(cart, repository.findOrCreate("c1"));
    }

    @Test
    void deleteByCustomerIdRemovesCart() {
        Cart cart = repository.findOrCreate("c1");
        repository.deleteByCustomerId("c1");
        Cart recreated = repository.findOrCreate("c1");
        assertEquals(cart.getCustomerId(), recreated.getCustomerId());
    }
}
