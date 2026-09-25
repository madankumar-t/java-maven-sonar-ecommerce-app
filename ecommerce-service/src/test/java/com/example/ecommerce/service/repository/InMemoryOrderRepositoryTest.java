package com.example.ecommerce.service.repository;

import com.example.ecommerce.common.model.Order;
import com.example.ecommerce.common.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    private Order sampleOrder(String id, String customerId) {
        List<OrderItem> items = Collections.singletonList(new OrderItem("p1", "Laptop", BigDecimal.TEN, 1));
        return new Order(id, customerId, items, BigDecimal.TEN);
    }

    @Test
    void savesAndFindsOrder() {
        Order order = sampleOrder("o1", "c1");
        repository.save(order);
        assertTrue(repository.findById("o1").isPresent());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        assertFalse(repository.findById("missing").isPresent());
    }

    @Test
    void findByCustomerIdFiltersCorrectly() {
        repository.save(sampleOrder("o1", "c1"));
        repository.save(sampleOrder("o2", "c2"));

        List<Order> orders = repository.findByCustomerId("c1");
        assertEquals(1, orders.size());
        assertEquals("o1", orders.get(0).getId());
    }

    @Test
    void findAllReturnsAllOrders() {
        repository.save(sampleOrder("o1", "c1"));
        repository.save(sampleOrder("o2", "c2"));
        assertEquals(2, repository.findAll().size());
    }
}
