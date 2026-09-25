package com.example.ecommerce.service.service;

import java.math.BigDecimal;

public class PaymentService {

    public enum PaymentResult {
        SUCCESS,
        DECLINED,
        INVALID_AMOUNT
    }

    public PaymentResult processPayment(String customerId, BigDecimal amount, String paymentMethod) {
        if (amount == null || amount.signum() <= 0) {
            return PaymentResult.INVALID_AMOUNT;
        }
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return PaymentResult.DECLINED;
        }
        if (!isSupportedMethod(paymentMethod)) {
            return PaymentResult.DECLINED;
        }
        return PaymentResult.SUCCESS;
    }

    private boolean isSupportedMethod(String paymentMethod) {
        String normalized = paymentMethod.trim().toUpperCase();
        return normalized.equals("CREDIT_CARD")
                || normalized.equals("DEBIT_CARD")
                || normalized.equals("PAYPAL")
                || normalized.equals("WALLET");
    }
}
