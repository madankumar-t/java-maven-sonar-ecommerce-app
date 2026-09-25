package com.example.ecommerce.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {

    private final Product laptop = new Product("p1", "Laptop", BigDecimal.valueOf(1000), 5, "Electronics");
    private final Product mouse = new Product("p2", "Mouse", BigDecimal.valueOf(25), 20, "Electronics");

    @Test
    void rejectsEmptyCustomerId() {
        assertThrows(IllegalArgumentException.class, () -> new Cart(""));
    }

    @Test
    void newCartIsEmpty() {
        Cart cart = new Cart("c1");
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalItemCount());
    }

    @Test
    void addItemAddsNewProduct() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        assertFalse(cart.isEmpty());
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void addItemIncrementsExistingProduct() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        cart.addItem(laptop, 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.findItem("p1").get().getQuantity());
    }

    @Test
    void removeItemDeletesProduct() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        cart.removeItem("p1");
        assertTrue(cart.isEmpty());
    }

    @Test
    void updateQuantityChangesExistingItem() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        cart.updateQuantity("p1", 4);
        assertEquals(4, cart.findItem("p1").get().getQuantity());
    }

    @Test
    void updateQuantityRejectsMissingProduct() {
        Cart cart = new Cart("c1");
        assertThrows(IllegalArgumentException.class, () -> cart.updateQuantity("missing", 1));
    }

    @Test
    void clearRemovesAllItems() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        cart.addItem(mouse, 2);
        cart.clear();
        assertTrue(cart.isEmpty());
    }

    @Test
    void getSubtotalSumsLineTotals() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        cart.addItem(mouse, 2);
        assertEquals(BigDecimal.valueOf(1050), cart.getSubtotal());
    }

    @Test
    void getTotalItemCountSumsQuantities() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        cart.addItem(mouse, 2);
        assertEquals(3, cart.getTotalItemCount());
    }

    @Test
    void getItemsReturnsDefensiveCopy() {
        Cart cart = new Cart("c1");
        cart.addItem(laptop, 1);
        cart.getItems().clear();
        assertFalse(cart.isEmpty());
    }
}
