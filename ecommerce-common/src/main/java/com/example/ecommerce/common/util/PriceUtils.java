package com.example.ecommerce.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceUtils {

    private PriceUtils() {
    }

    public static BigDecimal round(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal applyPercentageDiscount(BigDecimal amount, BigDecimal percentage) {
        if (amount == null || percentage == null) {
            throw new IllegalArgumentException("Amount and percentage must not be null");
        }
        if (percentage.signum() < 0 || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        BigDecimal discountFactor = BigDecimal.ONE.subtract(
                percentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return round(amount.multiply(discountFactor));
    }

    public static boolean isPositive(BigDecimal amount) {
        return amount != null && amount.signum() > 0;
    }
}
