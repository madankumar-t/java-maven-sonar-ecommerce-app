package com.example.ecommerce.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private List<OrderItem> items() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("p1", "Laptop", BigDecimal.valueOf(1000), 1));
        return items;
    }

    @Test
    void createsOrderWithCreatedStatus() {
        Order order = new Order("o1", "c1", items(), BigDecimal.valueOf(1000));
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals("o1", order.getId());
        assertEquals("c1", order.getCustomerId());
        assertEquals(BigDecimal.valueOf(1000), order.getTotalAmount());
    }

    @Test
    void rejectsEmptyItems() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order("o1", "c1", new ArrayList<>(), BigDecimal.ZERO));
    }

    @Test
    void markAsPaidTransitionsFromCreated() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        order.markAsPaid();
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void markAsPaidRejectsInvalidTransition() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        order.markAsPaid();
        assertThrows(IllegalStateException.class, order::markAsPaid);
    }

    @Test
    void shipRequiresPaidStatus() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        assertThrows(IllegalStateException.class, order::ship);
        order.markAsPaid();
        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void deliverRequiresShippedStatus() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        assertThrows(IllegalStateException.class, order::deliver);
        order.markAsPaid();
        order.ship();
        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void cancelWorksBeforeShipping() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancelRejectsAfterShipping() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        order.markAsPaid();
        order.ship();
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void itemsAreImmutable() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        assertThrows(UnsupportedOperationException.class, () -> order.getItems().add(
                new OrderItem("p2", "Mouse", BigDecimal.ONE, 1)));
    }

    @Test
    void equalsAndHashCodeBasedOnId() {
        Order a = new Order("o1", "c1", items(), BigDecimal.TEN);
        Order b = new Order("o1", "c2", items(), BigDecimal.ONE);
        Order c = new Order("o2", "c1", items(), BigDecimal.TEN);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, "not an order");
    }

    @Test
    void createdAtIsSet() {
        Order order = new Order("o1", "c1", items(), BigDecimal.TEN);
        assertNotEquals(null, order.getCreatedAt());
    }
}
