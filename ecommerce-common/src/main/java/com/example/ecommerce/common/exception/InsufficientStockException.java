package com.example.ecommerce.common.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productId, int requested, int available) {
        super("Insufficient stock for product " + productId + ": requested " + requested
                + " but only " + available + " available");
    }
}
