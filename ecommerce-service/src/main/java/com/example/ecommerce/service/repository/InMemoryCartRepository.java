package com.example.ecommerce.service.repository;

import com.example.ecommerce.common.model.Cart;

import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryCartRepository {

    private final Map<String, Cart> carts = new LinkedHashMap<>();

    public Cart findOrCreate(String customerId) {
        return carts.computeIfAbsent(customerId, Cart::new);
    }

    public void save(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    public void deleteByCustomerId(String customerId) {
        carts.remove(customerId);
    }
}
