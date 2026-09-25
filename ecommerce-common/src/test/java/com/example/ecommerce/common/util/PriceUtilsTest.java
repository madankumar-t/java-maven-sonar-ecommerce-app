package com.example.ecommerce.common.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceUtilsTest {

    @Test
    void roundsToTwoDecimalPlaces() {
        assertEquals(BigDecimal.valueOf(10.13), PriceUtils.round(BigDecimal.valueOf(10.125)));
    }

    @Test
    void roundRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> PriceUtils.round(null));
    }

    @Test
    void appliesPercentageDiscount() {
        BigDecimal result = PriceUtils.applyPercentageDiscount(BigDecimal.valueOf(200), BigDecimal.valueOf(10));
        assertEquals(BigDecimal.valueOf(180.00).setScale(2), result);
    }

    @Test
    void applyPercentageDiscountRejectsNullAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> PriceUtils.applyPercentageDiscount(null, BigDecimal.TEN));
    }

    @Test
    void applyPercentageDiscountRejectsNullPercentage() {
        assertThrows(IllegalArgumentException.class,
                () -> PriceUtils.applyPercentageDiscount(BigDecimal.TEN, null));
    }

    @Test
    void applyPercentageDiscountRejectsOutOfRangeValue() {
        assertThrows(IllegalArgumentException.class,
                () -> PriceUtils.applyPercentageDiscount(BigDecimal.TEN, BigDecimal.valueOf(101)));
        assertThrows(IllegalArgumentException.class,
                () -> PriceUtils.applyPercentageDiscount(BigDecimal.TEN, BigDecimal.valueOf(-1)));
    }

    @Test
    void isPositiveDetectsPositiveAmounts() {
        assertTrue(PriceUtils.isPositive(BigDecimal.ONE));
        assertFalse(PriceUtils.isPositive(BigDecimal.ZERO));
        assertFalse(PriceUtils.isPositive(BigDecimal.valueOf(-1)));
        assertFalse(PriceUtils.isPositive(null));
    }
}
