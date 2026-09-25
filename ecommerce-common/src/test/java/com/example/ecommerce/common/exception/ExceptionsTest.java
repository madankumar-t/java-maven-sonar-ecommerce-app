package com.example.ecommerce.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionsTest {

    @Test
    void productNotFoundExceptionMessage() {
        ProductNotFoundException ex = new ProductNotFoundException("p1");
        assertTrue(ex.getMessage().contains("p1"));
    }

    @Test
    void insufficientStockExceptionMessage() {
        InsufficientStockException ex = new InsufficientStockException("p1", 5, 2);
        assertTrue(ex.getMessage().contains("p1"));
        assertTrue(ex.getMessage().contains("5"));
        assertTrue(ex.getMessage().contains("2"));
    }

    @Test
    void invalidQuantityExceptionMessage() {
        InvalidQuantityException ex = new InvalidQuantityException(-1);
        assertTrue(ex.getMessage().contains("-1"));
    }

    @Test
    void emptyCartExceptionMessage() {
        EmptyCartException ex = new EmptyCartException("c1");
        assertTrue(ex.getMessage().contains("c1"));
    }

    @Test
    void customerNotFoundExceptionMessage() {
        CustomerNotFoundException ex = new CustomerNotFoundException("c1");
        assertTrue(ex.getMessage().contains("c1"));
    }
}
