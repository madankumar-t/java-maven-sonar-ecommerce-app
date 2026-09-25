package com.example.ecommerce.common.exception;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(int quantity) {
        super("Invalid quantity: " + quantity);
    }
}
