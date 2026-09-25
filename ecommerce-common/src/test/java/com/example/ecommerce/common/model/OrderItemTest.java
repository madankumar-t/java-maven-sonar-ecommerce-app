package com.example.ecommerce.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {

    @Test
    void createsOrderItem() {
        OrderItem item = new OrderItem("p1", "Laptop", BigDecimal.valueOf(100), 2);
        assertEquals("p1", item.getProductId());
        assertEquals("Laptop", item.getProductName());
        assertEquals(BigDecimal.valueOf(100), item.getUnitPrice());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void rejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem("p1", "Laptop", BigDecimal.TEN, 0));
    }

    @Test
    void getLineTotalMultipliesPriceAndQuantity() {
        OrderItem item = new OrderItem("p1", "Laptop", BigDecimal.valueOf(50), 3);
        assertEquals(BigDecimal.valueOf(150), item.getLineTotal());
    }

    @Test
    void equalsAndHashCode() {
        OrderItem a = new OrderItem("p1", "Laptop", BigDecimal.TEN, 2);
        OrderItem b = new OrderItem("p1", "Laptop", BigDecimal.TEN, 2);
        OrderItem c = new OrderItem("p2", "Mouse", BigDecimal.ONE, 1);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, "not an order item");
    }

    @Test
    void listOfItemsCanBeUsedInOrder() {
        List<OrderItem> items = Collections.singletonList(new OrderItem("p1", "Laptop", BigDecimal.TEN, 1));
        assertEquals(1, items.size());
    }
}
