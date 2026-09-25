package com.example.ecommerce.service.service;

import com.example.ecommerce.common.exception.InsufficientStockException;
import com.example.ecommerce.common.exception.InvalidQuantityException;
import com.example.ecommerce.common.model.Cart;
import com.example.ecommerce.common.model.Product;
import com.example.ecommerce.service.repository.InMemoryCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ProductService productService;

    private InMemoryCartRepository cartRepository;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartRepository = new InMemoryCartRepository();
        cartService = new CartService(cartRepository, productService);
    }

    @Test
    void addToCartAddsProductWhenStockAvailable() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productService.getProduct("p1")).thenReturn(product);

        Cart cart = cartService.addToCart("c1", "p1", 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.findItem("p1").get().getQuantity());
    }

    @Test
    void addToCartRejectsNonPositiveQuantity() {
        assertThrows(InvalidQuantityException.class, () -> cartService.addToCart("c1", "p1", 0));
    }

    @Test
    void addToCartRejectsInsufficientStock() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 1, "Electronics");
        when(productService.getProduct("p1")).thenReturn(product);

        assertThrows(InsufficientStockException.class, () -> cartService.addToCart("c1", "p1", 5));
    }

    @Test
    void removeFromCartDeletesItem() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productService.getProduct("p1")).thenReturn(product);

        cartService.addToCart("c1", "p1", 2);
        Cart cart = cartService.removeFromCart("c1", "p1");

        assertTrue(cart.isEmpty());
    }

    @Test
    void updateQuantityChangesExistingItem() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productService.getProduct("p1")).thenReturn(product);

        cartService.addToCart("c1", "p1", 2);
        Cart cart = cartService.updateQuantity("c1", "p1", 4);

        assertEquals(4, cart.findItem("p1").get().getQuantity());
    }

    @Test
    void updateQuantityRejectsNonPositiveValue() {
        assertThrows(InvalidQuantityException.class, () -> cartService.updateQuantity("c1", "p1", 0));
    }

    @Test
    void updateQuantityRejectsInsufficientStock() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 2, "Electronics");
        when(productService.getProduct("p1")).thenReturn(product);

        assertThrows(InsufficientStockException.class, () -> cartService.updateQuantity("c1", "p1", 5));
    }

    @Test
    void clearCartEmptiesCart() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productService.getProduct("p1")).thenReturn(product);

        cartService.addToCart("c1", "p1", 2);
        cartService.clearCart("c1");

        assertTrue(cartService.getCart("c1").isEmpty());
    }

    @Test
    void getSubtotalReturnsCartSubtotal() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productService.getProduct("p1")).thenReturn(product);

        cartService.addToCart("c1", "p1", 3);

        assertEquals(BigDecimal.valueOf(30), cartService.getSubtotal("c1"));
    }
}
