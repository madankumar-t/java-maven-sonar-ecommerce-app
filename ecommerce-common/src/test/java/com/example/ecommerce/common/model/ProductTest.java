package com.example.ecommerce.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    @Test
    void createsProductWithValidData() {
        Product product = new Product("p1", "Laptop", BigDecimal.valueOf(999.99), 10, "Electronics");

        assertEquals("p1", product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(BigDecimal.valueOf(999.99), product.getPrice());
        assertEquals(10, product.getStockQuantity());
        assertEquals("Electronics", product.getCategory());
    }

    @Test
    void rejectsEmptyId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("", "Laptop", BigDecimal.TEN, 1, "Electronics"));
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("p1", "Laptop", BigDecimal.valueOf(-1), 1, "Electronics"));
    }

    @Test
    void rejectsNegativeStock() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product("p1", "Laptop", BigDecimal.TEN, -1, "Electronics"));
    }

    @Test
    void setNameUpdatesValue() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 1, "Electronics");
        product.setName("Gaming Laptop");
        assertEquals("Gaming Laptop", product.getName());
    }

    @Test
    void setPriceRejectsNegativeValue() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 1, "Electronics");
        assertThrows(IllegalArgumentException.class, () -> product.setPrice(BigDecimal.valueOf(-5)));
    }

    @Test
    void setStockQuantityRejectsNegativeValue() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 1, "Electronics");
        assertThrows(IllegalArgumentException.class, () -> product.setStockQuantity(-1));
    }

    @Test
    void setCategoryUpdatesValue() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 1, "Electronics");
        product.setCategory("Computers");
        assertEquals("Computers", product.getCategory());
    }

    @Test
    void hasStockReturnsTrueWhenEnough() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        assertTrue(product.hasStock(5));
        assertFalse(product.hasStock(6));
    }

    @Test
    void reduceStockDecreasesQuantity() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        product.reduceStock(3);
        assertEquals(2, product.getStockQuantity());
    }

    @Test
    void reduceStockRejectsNonPositiveQuantity() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        assertThrows(IllegalArgumentException.class, () -> product.reduceStock(0));
    }

    @Test
    void reduceStockRejectsInsufficientStock() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 2, "Electronics");
        assertThrows(IllegalStateException.class, () -> product.reduceStock(3));
    }

    @Test
    void increaseStockAddsQuantity() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 2, "Electronics");
        product.increaseStock(3);
        assertEquals(5, product.getStockQuantity());
    }

    @Test
    void increaseStockRejectsNonPositiveQuantity() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 2, "Electronics");
        assertThrows(IllegalArgumentException.class, () -> product.increaseStock(0));
    }

    @Test
    void equalsAndHashCodeBasedOnId() {
        Product a = new Product("p1", "Laptop", BigDecimal.TEN, 2, "Electronics");
        Product b = new Product("p1", "Different Name", BigDecimal.ONE, 5, "Other");
        Product c = new Product("p2", "Laptop", BigDecimal.TEN, 2, "Electronics");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, "not a product");
        assertEquals(a, a);
    }

    @Test
    void toStringContainsFields() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 2, "Electronics");
        assertTrue(product.toString().contains("p1"));
    }
}
