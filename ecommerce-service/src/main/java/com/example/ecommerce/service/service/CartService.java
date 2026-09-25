package com.example.ecommerce.service.service;

import com.example.ecommerce.common.exception.InsufficientStockException;
import com.example.ecommerce.common.exception.InvalidQuantityException;
import com.example.ecommerce.common.model.Cart;
import com.example.ecommerce.common.model.Product;
import com.example.ecommerce.service.repository.InMemoryCartRepository;

import java.math.BigDecimal;

public class CartService {

    private final InMemoryCartRepository cartRepository;
    private final ProductService productService;

    public CartService(InMemoryCartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart getCart(String customerId) {
        return cartRepository.findOrCreate(customerId);
    }

    public Cart addToCart(String customerId, String productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
        Product product = productService.getProduct(productId);
        if (!product.hasStock(quantity)) {
            throw new InsufficientStockException(productId, quantity, product.getStockQuantity());
        }
        Cart cart = cartRepository.findOrCreate(customerId);
        cart.addItem(product, quantity);
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeFromCart(String customerId, String productId) {
        Cart cart = cartRepository.findOrCreate(customerId);
        cart.removeItem(productId);
        cartRepository.save(cart);
        return cart;
    }

    public Cart updateQuantity(String customerId, String productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
        Product product = productService.getProduct(productId);
        if (!product.hasStock(quantity)) {
            throw new InsufficientStockException(productId, quantity, product.getStockQuantity());
        }
        Cart cart = cartRepository.findOrCreate(customerId);
        cart.updateQuantity(productId, quantity);
        cartRepository.save(cart);
        return cart;
    }

    public void clearCart(String customerId) {
        Cart cart = cartRepository.findOrCreate(customerId);
        cart.clear();
        cartRepository.save(cart);
    }

    public BigDecimal getSubtotal(String customerId) {
        return cartRepository.findOrCreate(customerId).getSubtotal();
    }
}
