package com.example.ecommerce.service.service;

import com.example.ecommerce.common.exception.EmptyCartException;
import com.example.ecommerce.common.model.Cart;
import com.example.ecommerce.common.model.CartItem;
import com.example.ecommerce.common.model.Order;
import com.example.ecommerce.common.model.OrderItem;
import com.example.ecommerce.common.model.Product;
import com.example.ecommerce.service.repository.InMemoryCartRepository;
import com.example.ecommerce.service.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderService {

    private final OrderRepository orderRepository;
    private final InMemoryCartRepository cartRepository;
    private final ProductService productService;
    private final DiscountService discountService;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository,
                         InMemoryCartRepository cartRepository,
                         ProductService productService,
                         DiscountService discountService,
                         PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.discountService = discountService;
        this.paymentService = paymentService;
    }

    public Order checkout(String customerId, String paymentMethod, String couponCode, boolean loyalCustomer) {
        Cart cart = cartRepository.findOrCreate(customerId);
        if (cart.isEmpty()) {
            throw new EmptyCartException(customerId);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            Product product = productService.getProduct(cartItem.getProduct().getId());
            orderItems.add(new OrderItem(product.getId(), product.getName(), product.getPrice(), cartItem.getQuantity()));
        }

        BigDecimal subtotal = cart.getSubtotal();
        BigDecimal afterBulkDiscount = discountService.applyBulkDiscount(subtotal);
        BigDecimal afterLoyaltyDiscount = discountService.applyLoyaltyDiscount(afterBulkDiscount, loyalCustomer);
        BigDecimal finalAmount = discountService.applyCouponCode(afterLoyaltyDiscount, couponCode);

        PaymentService.PaymentResult result = paymentService.processPayment(customerId, finalAmount, paymentMethod);
        if (result != PaymentService.PaymentResult.SUCCESS) {
            throw new IllegalStateException("Payment failed with result: " + result);
        }

        for (CartItem cartItem : cart.getItems()) {
            productService.getProduct(cartItem.getProduct().getId()).reduceStock(cartItem.getQuantity());
        }

        Order order = new Order(UUID.randomUUID().toString(), customerId, orderItems, finalAmount);
        order.markAsPaid();
        orderRepository.save(order);

        cart.clear();
        cartRepository.save(cart);

        return order;
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public List<Order> getOrdersForCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order cancelOrder(String orderId) {
        Order order = getOrder(orderId);
        order.cancel();
        orderRepository.save(order);
        return order;
    }
}
