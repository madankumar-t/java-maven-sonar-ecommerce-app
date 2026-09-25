package com.example.ecommerce.service.service;

import com.example.ecommerce.common.exception.EmptyCartException;
import com.example.ecommerce.common.model.Order;
import com.example.ecommerce.common.model.OrderStatus;
import com.example.ecommerce.common.model.Product;
import com.example.ecommerce.service.repository.InMemoryCartRepository;
import com.example.ecommerce.service.repository.InMemoryOrderRepository;
import com.example.ecommerce.service.repository.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest {

    private InMemoryProductRepository productRepository;
    private InMemoryCartRepository cartRepository;
    private InMemoryOrderRepository orderRepository;
    private ProductService productService;
    private CartService cartService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        cartRepository = new InMemoryCartRepository();
        orderRepository = new InMemoryOrderRepository();
        productService = new ProductService(productRepository);
        cartService = new CartService(cartRepository, productService);
        orderService = new OrderService(orderRepository, cartRepository, productService,
                new DiscountService(), new PaymentService());

        productRepository.save(new Product("p1", "Laptop", BigDecimal.valueOf(1000), 5, "Electronics"));
        productRepository.save(new Product("p2", "Mouse", BigDecimal.valueOf(25), 20, "Electronics"));
    }

    @Test
    void checkoutRejectsEmptyCart() {
        assertThrows(EmptyCartException.class,
                () -> orderService.checkout("c1", "CREDIT_CARD", null, false));
    }

    @Test
    void checkoutCreatesPaidOrderAndReducesStock() {
        cartService.addToCart("c1", "p1", 1);
        cartService.addToCart("c1", "p2", 2);

        Order order = orderService.checkout("c1", "CREDIT_CARD", null, false);

        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(2, order.getItems().size());
        assertEquals(4, productService.getProduct("p1").getStockQuantity());
        assertEquals(18, productService.getProduct("p2").getStockQuantity());
        assertTrue(cartService.getCart("c1").isEmpty());
    }

    @Test
    void checkoutAppliesCouponDiscount() {
        cartService.addToCart("c1", "p2", 1);
        Order order = orderService.checkout("c1", "CREDIT_CARD", "SAVE10", false);

        assertEquals(BigDecimal.valueOf(22.50).setScale(2), order.getTotalAmount());
    }

    @Test
    void checkoutFailsWhenPaymentDeclined() {
        cartService.addToCart("c1", "p2", 1);
        assertThrows(IllegalStateException.class,
                () -> orderService.checkout("c1", "UNSUPPORTED_METHOD", null, false));
    }

    @Test
    void getOrderReturnsSavedOrder() {
        cartService.addToCart("c1", "p2", 1);
        Order order = orderService.checkout("c1", "CREDIT_CARD", null, false);

        assertEquals(order, orderService.getOrder(order.getId()));
    }

    @Test
    void getOrderThrowsWhenMissing() {
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrder("missing"));
    }

    @Test
    void getOrdersForCustomerReturnsAllOrdersOfCustomer() {
        cartService.addToCart("c1", "p2", 1);
        orderService.checkout("c1", "CREDIT_CARD", null, false);

        cartService.addToCart("c1", "p1", 1);
        orderService.checkout("c1", "CREDIT_CARD", null, false);

        List<Order> orders = orderService.getOrdersForCustomer("c1");
        assertEquals(2, orders.size());
    }

    @Test
    void cancelOrderChangesStatusToCancelled() {
        cartService.addToCart("c1", "p2", 1);
        Order order = orderService.checkout("c1", "CREDIT_CARD", null, false);

        Order cancelled = orderService.cancelOrder(order.getId());
        assertEquals(OrderStatus.CANCELLED, cancelled.getStatus());
    }
}
