package com.example.ecommerce.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerTest {

    @Test
    void createsCustomerWithValidData() {
        Customer customer = new Customer("c1", "Jane Doe", "jane@example.com");
        assertEquals("c1", customer.getId());
        assertEquals("Jane Doe", customer.getName());
        assertEquals("jane@example.com", customer.getEmail());
    }

    @Test
    void rejectsEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> new Customer("", "Jane", "jane@example.com"));
    }

    @Test
    void rejectsInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Customer("c1", "Jane", "invalid-email"));
    }

    @Test
    void setNameUpdatesValue() {
        Customer customer = new Customer("c1", "Jane", "jane@example.com");
        customer.setName("Janet");
        assertEquals("Janet", customer.getName());
    }

    @Test
    void setEmailRejectsInvalidValue() {
        Customer customer = new Customer("c1", "Jane", "jane@example.com");
        assertThrows(IllegalArgumentException.class, () -> customer.setEmail("bad"));
    }

    @Test
    void setEmailUpdatesValidValue() {
        Customer customer = new Customer("c1", "Jane", "jane@example.com");
        customer.setEmail("new@example.com");
        assertEquals("new@example.com", customer.getEmail());
    }

    @Test
    void equalsAndHashCodeBasedOnId() {
        Customer a = new Customer("c1", "Jane", "jane@example.com");
        Customer b = new Customer("c1", "Other", "other@example.com");
        Customer c = new Customer("c2", "Jane", "jane@example.com");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, "not a customer");
    }

    @Test
    void toStringContainsFields() {
        Customer customer = new Customer("c1", "Jane", "jane@example.com");
        assertTrue(customer.toString().contains("c1"));
    }
}
