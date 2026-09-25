package com.example.ecommerce.service.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountServiceTest {

    private final DiscountService discountService = new DiscountService();

    @Test
    void appliesBulkDiscountWhenThresholdMet() {
        BigDecimal result = discountService.applyBulkDiscount(BigDecimal.valueOf(600));
        assertEquals(BigDecimal.valueOf(540.00).setScale(2), result);
    }

    @Test
    void noBulkDiscountBelowThreshold() {
        BigDecimal result = discountService.applyBulkDiscount(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), result);
    }

    @Test
    void applyBulkDiscountRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> discountService.applyBulkDiscount(null));
    }

    @Test
    void appliesLoyaltyDiscountForLoyalCustomer() {
        BigDecimal result = discountService.applyLoyaltyDiscount(BigDecimal.valueOf(100), true);
        assertEquals(BigDecimal.valueOf(95.00).setScale(2), result);
    }

    @Test
    void noLoyaltyDiscountForRegularCustomer() {
        BigDecimal result = discountService.applyLoyaltyDiscount(BigDecimal.valueOf(100), false);
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), result);
    }

    @Test
    void applyLoyaltyDiscountRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> discountService.applyLoyaltyDiscount(null, true));
    }

    @Test
    void appliesCouponSave10() {
        BigDecimal result = discountService.applyCouponCode(BigDecimal.valueOf(100), "SAVE10");
        assertEquals(BigDecimal.valueOf(90.00).setScale(2), result);
    }

    @Test
    void appliesCouponSave20() {
        BigDecimal result = discountService.applyCouponCode(BigDecimal.valueOf(100), "save20");
        assertEquals(BigDecimal.valueOf(80.00).setScale(2), result);
    }

    @Test
    void unknownCouponHasNoEffect() {
        BigDecimal result = discountService.applyCouponCode(BigDecimal.valueOf(100), "UNKNOWN");
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), result);
    }

    @Test
    void nullOrEmptyCouponHasNoEffect() {
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), discountService.applyCouponCode(BigDecimal.valueOf(100), null));
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), discountService.applyCouponCode(BigDecimal.valueOf(100), ""));
    }

    @Test
    void applyCouponCodeRejectsNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> discountService.applyCouponCode(null, "SAVE10"));
    }
}
