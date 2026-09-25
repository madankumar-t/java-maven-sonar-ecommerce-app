package com.example.ecommerce.service.service;

import com.example.ecommerce.common.util.PriceUtils;

import java.math.BigDecimal;

public class DiscountService {

    private static final BigDecimal BULK_ORDER_THRESHOLD = BigDecimal.valueOf(500);
    private static final BigDecimal BULK_DISCOUNT_PERCENTAGE = BigDecimal.valueOf(10);
    private static final BigDecimal LOYALTY_DISCOUNT_PERCENTAGE = BigDecimal.valueOf(5);

    public BigDecimal applyBulkDiscount(BigDecimal subtotal) {
        if (subtotal == null) {
            throw new IllegalArgumentException("Subtotal must not be null");
        }
        if (subtotal.compareTo(BULK_ORDER_THRESHOLD) >= 0) {
            return PriceUtils.applyPercentageDiscount(subtotal, BULK_DISCOUNT_PERCENTAGE);
        }
        return PriceUtils.round(subtotal);
    }

    public BigDecimal applyLoyaltyDiscount(BigDecimal amount, boolean loyalCustomer) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        if (loyalCustomer) {
            return PriceUtils.applyPercentageDiscount(amount, LOYALTY_DISCOUNT_PERCENTAGE);
        }
        return PriceUtils.round(amount);
    }

    public BigDecimal applyCouponCode(BigDecimal amount, String couponCode) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        if (couponCode == null || couponCode.isEmpty()) {
            return PriceUtils.round(amount);
        }
        switch (couponCode.toUpperCase()) {
            case "SAVE10":
                return PriceUtils.applyPercentageDiscount(amount, BigDecimal.TEN);
            case "SAVE20":
                return PriceUtils.applyPercentageDiscount(amount, BigDecimal.valueOf(20));
            default:
                return PriceUtils.round(amount);
        }
    }
}
