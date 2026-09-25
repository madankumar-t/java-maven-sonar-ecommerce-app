package com.example.ecommerce.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartItemTest {

    private final Product product = new Product("p1", "Laptop", BigDecimal.valueOf(100), 10, "Electronics");

    @Test
    void createsCartItem() {
        CartItem item = new CartItem(product, 2);
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void rejectsNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(null, 1));
    }

    @Test
    void rejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(product, 0));
    }

    @Test
    void setQuantityRejectsNonPositiveValue() {
        CartItem item = new CartItem(product, 2);
        assertThrows(IllegalArgumentException.class, () -> item.setQuantity(0));
    }

    @Test
    void incrementQuantityIncreasesValue() {
        CartItem item = new CartItem(product, 2);
        item.incrementQuantity(3);
        assertEquals(5, item.getQuantity());
    }

    @Test
    void incrementQuantityRejectsNonPositiveAmount() {
        CartItem item = new CartItem(product, 2);
        assertThrows(IllegalArgumentException.class, () -> item.incrementQuantity(0));
    }

    @Test
    void getLineTotalMultipliesPriceByQuantity() {
        CartItem item = new CartItem(product, 3);
        assertEquals(BigDecimal.valueOf(300), item.getLineTotal());
    }

    @Test
    void equalsBasedOnProduct() {
        CartItem a = new CartItem(product, 2);
        CartItem b = new CartItem(product, 5);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toStringContainsProductId() {
        CartItem item = new CartItem(product, 2);
        assertEquals("CartItem{product=p1, quantity=2}", item.toString());
    }
}
