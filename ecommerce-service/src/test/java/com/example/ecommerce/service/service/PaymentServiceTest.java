package com.example.ecommerce.service.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentServiceTest {

    private final PaymentService paymentService = new PaymentService();

    @Test
    void successfulPaymentWithCreditCard() {
        PaymentService.PaymentResult result =
                paymentService.processPayment("c1", BigDecimal.valueOf(100), "CREDIT_CARD");
        assertEquals(PaymentService.PaymentResult.SUCCESS, result);
    }

    @Test
    void successfulPaymentIsCaseInsensitive() {
        PaymentService.PaymentResult result =
                paymentService.processPayment("c1", BigDecimal.valueOf(100), "paypal");
        assertEquals(PaymentService.PaymentResult.SUCCESS, result);
    }

    @Test
    void invalidAmountReturnsInvalidAmount() {
        assertEquals(PaymentService.PaymentResult.INVALID_AMOUNT,
                paymentService.processPayment("c1", BigDecimal.ZERO, "CREDIT_CARD"));
        assertEquals(PaymentService.PaymentResult.INVALID_AMOUNT,
                paymentService.processPayment("c1", null, "CREDIT_CARD"));
        assertEquals(PaymentService.PaymentResult.INVALID_AMOUNT,
                paymentService.processPayment("c1", BigDecimal.valueOf(-10), "CREDIT_CARD"));
    }

    @Test
    void emptyPaymentMethodIsDeclined() {
        assertEquals(PaymentService.PaymentResult.DECLINED,
                paymentService.processPayment("c1", BigDecimal.TEN, ""));
        assertEquals(PaymentService.PaymentResult.DECLINED,
                paymentService.processPayment("c1", BigDecimal.TEN, "   "));
        assertEquals(PaymentService.PaymentResult.DECLINED,
                paymentService.processPayment("c1", BigDecimal.TEN, null));
    }

    @Test
    void unsupportedPaymentMethodIsDeclined() {
        assertEquals(PaymentService.PaymentResult.DECLINED,
                paymentService.processPayment("c1", BigDecimal.TEN, "BITCOIN"));
    }

    @Test
    void allSupportedMethodsSucceed() {
        assertEquals(PaymentService.PaymentResult.SUCCESS,
                paymentService.processPayment("c1", BigDecimal.TEN, "DEBIT_CARD"));
        assertEquals(PaymentService.PaymentResult.SUCCESS,
                paymentService.processPayment("c1", BigDecimal.TEN, "WALLET"));
    }
}
